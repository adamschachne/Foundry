package org.neuinfo.foundry.river;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.apache.log4j.Logger;
import org.bson.BasicBSONObject;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.neuinfo.foundry.river.util.MongoDBHelper;
import org.neuinfo.foundry.common.util.Utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by bozyurt on 4/5/14.
 */
public class Slurper implements Runnable {
    static Logger logger = Logger.getLogger(Slurper.class);
    private final MongoDBRiverDefinition definition;
    private final Context context;
    private final BasicDBObject findKeys;
    private final String gridfsOplogNamespace;
    private final String cmdOplogNamespace;
    private Mongo mongo;
    private DB slurpedDb;
    private DB oplogDb;
    private DBCollection oplogCollection;
    private final AtomicLong totalDocuments = new AtomicLong();
    private Set<String> oplogOperations = new HashSet<String>(11);
    // IBO
    private BSONTimestamp lastCheckpointTS = null;

    public Slurper(List<ServerAddress> mongoServers, MongoDBRiverDefinition definition,
                   Context context) {
        this(mongoServers, definition, context, null);
    }

    public Slurper(List<ServerAddress> mongoServers, MongoDBRiverDefinition definition,
                   Context context, BSONTimestamp lastCheckpointTS) {
        this.definition = definition;
        this.context = context;
        this.lastCheckpointTS = lastCheckpointTS;
        this.mongo = new MongoClient(mongoServers, definition.getMongoClientOptions());
        this.findKeys = new BasicDBObject();
        this.gridfsOplogNamespace = definition.getMongoOplogNamespace() + MongoDBRiver.GRIDFS_FILES_SUFFIX;
        this.cmdOplogNamespace = definition.getMongoDb() + "." + MongoDBRiver.OPLOG_NAMESPACE_COMMAND;
        if (definition.getExcludeFields() != null) {
            for (String key : definition.getExcludeFields()) {
                findKeys.put(key, 0);
            }
        } else if (definition.getIncludeFields() != null) {
            for (String key : definition.getIncludeFields()) {
                findKeys.put(key, 1);
            }
        }
        oplogOperations.add(MongoDBRiver.OPLOG_DELETE_OPERATION);
        oplogOperations.add(MongoDBRiver.OPLOG_UPDATE_OPERATION);
        oplogOperations.add(MongoDBRiver.OPLOG_INSERT_OPERATION);
        oplogOperations.add(MongoDBRiver.OPLOG_COMMAND_OPERATION);
    }

    @Override
    public void run() {
        while (context.getStatus() == Status.RUNNING) {
            try {
                if (!assignCollections()) {
                    break; // failed to assign oplogCollection or
                    // slurpedCollection
                }

                //TODO
                BSONTimestamp startTimestamp = null;
                if (this.lastCheckpointTS != null) {
                    startTimestamp = this.lastCheckpointTS;
                }

                /*
                if (!definition.isSkipInitialImport()) {
                    if (!riverHasIndexedFromOplog() && definition.getInitialTimestamp() == null) {
                        if (!isIndexEmpty()) {
                            // IBO
                            // MongoDBRiverHelper.setRiverStatus(client, definition.getRiverName(), Status.INITIAL_IMPORT_FAILED);
                            break;
                        }
                        if (definition.isImportAllCollections()) {
                            for (String name : slurpedDb.getCollectionNames()) {
                                DBCollection collection = slurpedDb.getCollection(name);
                                startTimestamp = doInitialImport(collection);
                            }
                        } else {
                            DBCollection collection = slurpedDb.getCollection(definition.getMongoCollection());
                            startTimestamp = doInitialImport(collection);
                        }
                    }
                } else {
                    logger.info("Skip initial import from collection " + definition.getMongoCollection());
                }
*/
                // Slurp from oplog
                DBCursor cursor = null;
                try {
                    cursor = oplogCursor(startTimestamp);
                    if (cursor == null) {
                        cursor = processFullOplog();
                    }
                    while (cursor.hasNext()) {
                        DBObject item = cursor.next();
                        startTimestamp = processOplogEntry(item, startTimestamp);
                    }
                    logger.debug("Before waiting for 500 ms");
                    Thread.sleep(500);
               /*
                } catch (MongoException.CursorNotFound e) {
                    logger.info(String.format("Cursor %s has been closed. About to open a new cursor.",
                            cursor.getCursorId()));
                    logger.debug(String.format("Total document inserted [%d]", totalDocuments.get()));
               */
                } catch (SlurperException sEx) {
                    logger.warn("Exception in slurper", sEx);
                    break;
                } catch (Exception ex) {
                    logger.warn("Exception while looping in cursor", ex);
                    Thread.currentThread().interrupt();
                    break;
                } finally {
                    if (cursor != null) {
                        logger.trace("Closing oplog cursor");
                        cursor.close();
                    }
                }

            } catch (MongoInterruptedException mIEx) {
                logger.warn("Mongo driver has been interrupted", mIEx);
                if (mongo != null) {
                    mongo.close();
                    mongo = null;
                }
                Thread.currentThread().interrupt();
                break;
            } catch (MongoException e) {
                logger.error("Mongo gave an exception", e);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException iEx) {
                }
            } catch (NoSuchElementException e) {
                logger.warn("A mongoDB cursor bug ?", e);
            }
        }
    }

    protected boolean riverHasIndexedFromOplog() {
        // IBO
        // return MongoDBRiver.getLastTimestamp(client, definition) != null;
        // IBO stub
        return false;
    }


    protected boolean isIndexEmpty() {
        // IBO
        //return MongoDBRiver.getIndexCount(client, definition) == 0;
        // IBO stub
        return true;
    }


    /**
     * Does an initial sync the same way MongoDB does.
     * https://groups.google.com/
     * forum/?fromgroups=#!topic/mongodb-user/sOKlhD_E2ns
     *
     * @return the last oplog timestamp before the import began
     * @throws InterruptedException if the blocking queue stream is interrupted while waiting
     */
    protected BSONTimestamp doInitialImport(DBCollection collection) throws InterruptedException {
        // TODO: ensure the index type is empty
        // DBCollection slurpedCollection =
        // slurpedDb.getCollection(definition.getMongoCollection());

        logger.info("MongoDBRiver is beginning initial import of " + collection.getFullName());
        BSONTimestamp startTimestamp = getCurrentOplogTimestamp();
        boolean inProgress = true;
        String lastId = null;
        while (inProgress) {
            DBCursor cursor = null;
            try {
                if (definition.isDisableIndexRefresh()) {
                    updateIndexRefresh(definition.getIndexName(), -1L);
                }
                if (!definition.isMongoGridFS()) {
                    logger.info(String.format("Collection % - count: %", collection.getName(), collection.count()));
                    long count = 0;
                    cursor = collection.find(getFilterForInitialImport(definition.getMongoCollectionFilter(), lastId));
                    while (cursor.hasNext()) {
                        DBObject object = cursor.next();
                        count++;
                        if (cursor.hasNext()) {
                            lastId = addInsertToStream(null, applyFieldFilter(object), collection.getName());
                        } else {
                            logger.debug("Last entry for initial import - add timestamp: " + startTimestamp);
                            lastId = addInsertToStream(startTimestamp, applyFieldFilter(object), collection.getName());
                        }
                    }
                    inProgress = false;
                    logger.info("Number documents indexed: " + count);
                } else {
                    // TODO: To be optimized.
                    // https://github.com/mongodb/mongo-java-driver/pull/48#issuecomment-25241988
                    // possible option: Get the object id list from .fs
                    // collection
                    // then call GriDFS.findOne
                    GridFS grid = new GridFS(mongo.getDB(definition.getMongoDb()), definition.getMongoCollection());

                    cursor = grid.getFileList();
                    while (cursor.hasNext()) {
                        DBObject object = cursor.next();
                        if (object instanceof GridFSDBFile) {
                            GridFSDBFile file = grid.findOne(new ObjectId(object.get(MongoDBRiver.MONGODB_ID_FIELD).toString()));
                            if (cursor.hasNext()) {
                                lastId = addInsertToStream(null, file);
                            } else {
                                logger.debug(String.format("Last entry for initial import - add timestamp: %s", startTimestamp));
                                lastId = addInsertToStream(startTimestamp, file);
                            }
                        }
                    }
                    inProgress = false;
                }
            /*
            } catch (MongoException.CursorNotFound e) {
                logger.info(String.format("Initial import - Cursor %d has been closed. About to open a new cursor.", cursor.getCursorId()));
                logger.debug(String.format("Total document inserted [%d]", totalDocuments.get()));
            */
            } finally {
                if (cursor != null) {
                    logger.trace("Closing initial import cursor");
                    cursor.close();
                }
                if (definition.isDisableIndexRefresh()) {
                    // IBO
                    //updateIndexRefresh(definition.getIndexName(), TimeValue.timeValueSeconds(1));
                }
            }
        }
        return startTimestamp;
    }

    private BasicDBObject getFilterForInitialImport(BasicDBObject filter, String id) {
        if (id == null) {
            return filter;
        } else {
            BasicDBObject filterId = new BasicDBObject(MongoDBRiver.MONGODB_ID_FIELD, new BasicBSONObject(QueryOperators.GT, id));
            if (filter == null) {
                return filterId;
            } else {
                //List<BasicDBObject> values = ImmutableList.of(filter, filterId);
                List<BasicDBObject> values = new ArrayList<BasicDBObject>(2);
                Collections.addAll(values, filter, filterId);
                values = Collections.unmodifiableList(values);
                return new BasicDBObject(QueryOperators.AND, values);
            }
        }
    }

    protected boolean assignCollections() {
        /*
        DB adminDb = mongo.getDB(MongoDBRiver.MONGODB_ADMIN_DATABASE);
        oplogDb = mongo.getDB(MongoDBRiver.MONGODB_LOCAL_DATABASE);

        if (!definition.getMongoAdminUser().isEmpty() && !definition.getMongoAdminPassword().isEmpty()) {
            logger.info(String.format("Authenticate %s with %s",
                    MongoDBRiver.MONGODB_ADMIN_DATABASE, definition.getMongoAdminUser()));

            CommandResult cmd = adminDb.authenticateCommand(definition.getMongoAdminUser(), definition.getMongoAdminPassword()
                    .toCharArray());
            if (!cmd.ok()) {
                logger.error(String.format("Authenticatication failed for %s: %s",
                        MongoDBRiver.MONGODB_ADMIN_DATABASE, cmd.getErrorMessage()));
                // Can still try with mongoLocal credential if provided.
                // return false;
            }
            oplogDb = adminDb.getMongo().getDB(MongoDBRiver.MONGODB_LOCAL_DATABASE);
        }

        if (!definition.getMongoLocalUser().isEmpty() && !definition.getMongoLocalPassword().isEmpty() && !oplogDb.isAuthenticated()) {
            logger.info(String.format("Authenticate %s with %s",
                    MongoDBRiver.MONGODB_LOCAL_DATABASE, definition.getMongoLocalUser()));
            CommandResult cmd = oplogDb.authenticateCommand(definition.getMongoLocalUser(), definition.getMongoLocalPassword()
                    .toCharArray());
            if (!cmd.ok()) {
                logger.error(String.format("Authentication failed for %s: %s", MongoDBRiver.MONGODB_LOCAL_DATABASE, cmd.getErrorMessage()));
                return false;
            }
        }

        Set<String> collections = oplogDb.getCollectionNames();
        if (!collections.contains(MongoDBRiver.OPLOG_COLLECTION)) {
            logger.error("Cannot find " + MongoDBRiver.OPLOG_COLLECTION + " collection. Please check this link: http://goo.gl/2x5IW");
            return false;
        }
        oplogCollection = oplogDb.getCollection(MongoDBRiver.OPLOG_COLLECTION);

        slurpedDb = mongo.getDB(definition.getMongoDb());
        if (!definition.getMongoAdminUser().isEmpty() && !definition.getMongoAdminPassword().isEmpty() && adminDb.isAuthenticated()) {
            slurpedDb = adminDb.getMongo().getDB(definition.getMongoDb());
        }
        */
        return true;
    }

    private void updateIndexRefresh(String name, Object value) {
        // IBO
        //    client.admin().indices().prepareUpdateSettings(name).setSettings(ImmutableMap.of("index.refresh_interval", value)).get();
    }

    private BSONTimestamp getCurrentOplogTimestamp() {
        return (BSONTimestamp) oplogCollection.find().sort(new BasicDBObject(MongoDBRiver.OPLOG_TIMESTAMP, -1)).limit(1).next()
                .get(MongoDBRiver.OPLOG_TIMESTAMP);
    }

    private DBCursor processFullOplog() throws InterruptedException, SlurperException {
        BSONTimestamp currentTimestamp = getCurrentOplogTimestamp();
        addInsertToStream(currentTimestamp, null);
        return oplogCursor(currentTimestamp);
    }

    private BSONTimestamp processOplogEntry(final DBObject entry, final BSONTimestamp startTimestamp) throws InterruptedException {
        if (!isValidOplogEntry(entry, startTimestamp)) {
            return startTimestamp;
        }
        Operation operation = Operation.fromString(entry.get(MongoDBRiver.OPLOG_OPERATION).toString());
        String namespace = entry.get(MongoDBRiver.OPLOG_NAMESPACE).toString();
        String collection = null;
        BSONTimestamp oplogTimestamp = (BSONTimestamp) entry.get(MongoDBRiver.OPLOG_TIMESTAMP);
        DBObject object = (DBObject) entry.get(MongoDBRiver.OPLOG_OBJECT);

        if (definition.isImportAllCollections()) {
            if (namespace.startsWith(definition.getMongoDb()) && !namespace.equals(cmdOplogNamespace)) {
                collection = getCollectionFromNamespace(namespace);
            }
        } else {
            collection = definition.getMongoCollection();
        }

        if (namespace.equals(cmdOplogNamespace)) {
            if (object.containsField(MongoDBRiver.OPLOG_DROP_COMMAND_OPERATION)) {
                operation = Operation.DROP_COLLECTION;
                if (definition.isImportAllCollections()) {
                    collection = object.get(MongoDBRiver.OPLOG_DROP_COMMAND_OPERATION).toString();
                    if (collection.startsWith("tmp.mr.")) {
                        return startTimestamp;
                    }
                }
            }
            if (object.containsField(MongoDBRiver.OPLOG_DROP_DATABASE_COMMAND_OPERATION)) {
                operation = Operation.DROP_DATABASE;
            }
        }

        logger.trace(String.format("namespace: %s - operation: %s", namespace, operation));
        if (namespace.equals(MongoDBRiver.OPLOG_ADMIN_COMMAND)) {
            if (operation == Operation.COMMAND) {
                processAdminCommandOplogEntry(entry, startTimestamp);
                return startTimestamp;
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("MongoDB object deserialized: " + object.toString());
            logger.trace("collection: " + collection);
            logger.trace(String.format("oplog entry - namespace [%s], operation [%s]", namespace, operation));
            logger.trace("oplog processing item " + entry);
        }

        String objectId = getObjectIdFromOplogEntry(entry);
        if (definition.isMongoGridFS() && namespace.endsWith(MongoDBRiver.GRIDFS_FILES_SUFFIX)
                && (operation == Operation.INSERT || operation == Operation.UPDATE)) {
            if (objectId == null) {
                throw new NullPointerException(MongoDBRiver.MONGODB_ID_FIELD);
            }
            GridFS grid = new GridFS(mongo.getDB(definition.getMongoDb()), collection);
            GridFSDBFile file = grid.findOne(new ObjectId(objectId));
            if (file != null) {
                logger.info(String.format("Caught file: %s - %s", file.getId(), file.getFilename()));
                object = file;
            } else {
                logger.warn("Cannot find file from id: " + objectId);
            }
        }

        if (object instanceof GridFSDBFile) {
            if (objectId == null) {
                throw new NullPointerException(MongoDBRiver.MONGODB_ID_FIELD);
            }
            if (logger.isTraceEnabled()) {
                logger.trace("Add attachment: " + objectId);
            }
            addToStream(operation, oplogTimestamp, applyFieldFilter(object), collection);
        } else {
            if (operation == Operation.UPDATE) {
                DBObject update = (DBObject) entry.get(MongoDBRiver.OPLOG_UPDATE);
                logger.debug("Updated item: " + update);
                addQueryToStream(operation, oplogTimestamp, update, collection);
            } else {
                if (operation == Operation.INSERT) {
                    addInsertToStream(oplogTimestamp, applyFieldFilter(object), collection);
                } else {
                    addToStream(operation, oplogTimestamp, applyFieldFilter(object), collection);
                }
            }
        }
        return oplogTimestamp;
    }

    private void processAdminCommandOplogEntry(final DBObject entry, final BSONTimestamp startTimestamp) throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("processAdminCommandOplogEntry - [%s]", entry));
        }
        DBObject object = (DBObject) entry.get(MongoDBRiver.OPLOG_OBJECT);
        if (definition.isImportAllCollections()) {
            if (object.containsField(MongoDBRiver.OPLOG_RENAME_COLLECTION_COMMAND_OPERATION) && object.containsField(MongoDBRiver.OPLOG_TO)) {
                String to = object.get(MongoDBRiver.OPLOG_TO).toString();
                if (to.startsWith(definition.getMongoDb())) {
                    String newCollection = getCollectionFromNamespace(to);
                    DBCollection coll = slurpedDb.getCollection(newCollection);
                    doInitialImport(coll);
                }
            }
        }
    }

    private String getCollectionFromNamespace(String namespace) {
        if (namespace.startsWith(definition.getMongoDb()) && Utils.numOfCharsIn(namespace, '.') == 1) {
            return namespace.substring(definition.getMongoDb().length() + 1);
        }
        logger.info(String.format("Cannot get collection from namespace [%s]", namespace));
        return null;
    }

    private boolean isValidOplogEntry(final DBObject entry, final BSONTimestamp startTimestamp) {
        String namespace = (String) entry.get(MongoDBRiver.OPLOG_NAMESPACE);
        // Initial support for sharded collection -
        // https://jira.mongodb.org/browse/SERVER-4333
        // Not interested in operation from migration or sharding
        if (entry.containsField(MongoDBRiver.OPLOG_FROM_MIGRATE) && ((BasicBSONObject) entry).getBoolean(MongoDBRiver.OPLOG_FROM_MIGRATE)) {
            logger.debug("[Invalid Oplog Entry] - from migration or sharding operation. Can be ignored. " + entry);
            return false;
        }
        // Not interested by chunks - skip all
        if (namespace.endsWith(MongoDBRiver.GRIDFS_CHUNKS_SUFFIX)) {
            return false;
        }

        if (startTimestamp != null) {
            BSONTimestamp oplogTimestamp = (BSONTimestamp) entry.get(MongoDBRiver.OPLOG_TIMESTAMP);
            if (oplogTimestamp.compareTo(startTimestamp) < 0) {
                logger.debug(String.format("[Invalid Oplog Entry] - entry timestamp [%s] before startTimestamp [%s]", entry, startTimestamp));
                return false;
            }
        }

        boolean validNamespace = false;
        if (definition.isMongoGridFS()) {
            validNamespace = gridfsOplogNamespace.equals(namespace);
        } else {
            if (definition.isImportAllCollections()) {
                // Skip temp entry generated by map / reduce
                if (namespace.startsWith(definition.getMongoDb()) && !namespace.startsWith(definition.getMongoDb() + ".tmp.mr")) {
                    validNamespace = true;
                }
            } else {
                if (definition.getMongoOplogNamespace().equals(namespace)) {
                    validNamespace = true;
                }
            }
            if (cmdOplogNamespace.equals(namespace)) {
                validNamespace = true;
            }

            if (MongoDBRiver.OPLOG_ADMIN_COMMAND.equals(namespace)) {
                validNamespace = true;
            }
        }
        if (!validNamespace) {
            logger.info(String.format("[Invalid Oplog Entry] - namespace [%s] is not valid", namespace));
            return false;
        }
        String operation = (String) entry.get(MongoDBRiver.OPLOG_OPERATION);
        if (!oplogOperations.contains(operation)) {
            logger.debug(String.format("[Invalid Oplog Entry] - operation [%s] is not valid", operation));
            return false;
        }

        // TODO: implement a better solution
        if (definition.getMongoOplogFilter() != null) {
            DBObject object = (DBObject) entry.get(MongoDBRiver.OPLOG_OBJECT);
            BasicDBObject filter = definition.getMongoOplogFilter();
            if (!filterMatch(filter, object)) {
                logger.debug(String.format("[Invalid Oplog Entry] - filter [%s] does not match object [%s]", filter, object));
                return false;
            }
        }
        return true;
    }

    private boolean filterMatch(DBObject filter, DBObject object) {
        for (String key : filter.keySet()) {
            if (!object.containsField(key)) {
                return false;
            }
            if (!filter.get(key).equals(object.get(key))) {
                return false;
            }
        }
        return true;
    }

    private DBObject applyFieldFilter(DBObject object) {
        if (object instanceof GridFSFile) {
            GridFSFile file = (GridFSFile) object;
            DBObject metadata = file.getMetaData();
            if (metadata != null) {
                file.setMetaData(applyFieldFilter(metadata));
            }
        } else {
            object = MongoDBHelper.applyExcludeFields(object, definition.getExcludeFields());
            object = MongoDBHelper.applyIncludeFields(object, definition.getIncludeFields());
        }
        return object;
    }

    /*
     * Extract "_id" from "o" if it fails try to extract from "o2"
     */
    private String getObjectIdFromOplogEntry(DBObject entry) {
        if (entry.containsField(MongoDBRiver.OPLOG_OBJECT)) {
            DBObject object = (DBObject) entry.get(MongoDBRiver.OPLOG_OBJECT);
            if (object.containsField(MongoDBRiver.MONGODB_ID_FIELD)) {
                return object.get(MongoDBRiver.MONGODB_ID_FIELD).toString();
            }
        }
        if (entry.containsField(MongoDBRiver.OPLOG_UPDATE)) {
            DBObject object = (DBObject) entry.get(MongoDBRiver.OPLOG_UPDATE);
            if (object.containsField(MongoDBRiver.MONGODB_ID_FIELD)) {
                return object.get(MongoDBRiver.MONGODB_ID_FIELD).toString();
            }
        }
        return null;
    }

    private DBObject getOplogFilter(final BSONTimestamp time) {
        BasicDBObject filter = new BasicDBObject();

        if (time == null) {
            logger.info("No known previous slurping time for this collection");
        } else {
            filter.put(MongoDBRiver.OPLOG_TIMESTAMP, new BasicDBObject(QueryOperators.GTE, time));
        }

        return filter;
    }

    private DBCursor oplogCursor(final BSONTimestamp timestampOverride) throws SlurperException {
        // FIXME orig
        //BSONTimestamp time = timestampOverride == null ? MongoDBRiver.getLastTimestamp(client, definition) : timestampOverride;
        BSONTimestamp time = timestampOverride;
        DBObject indexFilter = getOplogFilter(time);
        if (indexFilter == null) {
            return null;
        }

        int options = Bytes.QUERYOPTION_TAILABLE | Bytes.QUERYOPTION_AWAITDATA | Bytes.QUERYOPTION_NOTIMEOUT;

        // Using OPLOGREPLAY to improve performance:
        // https://jira.mongodb.org/browse/JAVA-771
        if (indexFilter.containsField(MongoDBRiver.OPLOG_TIMESTAMP)) {
            options = options | Bytes.QUERYOPTION_OPLOGREPLAY;
        }
        DBCursor cursor = oplogCollection.find(indexFilter).setOptions(options);
        isRiverStale(cursor, time);
        return cursor;
    }

    private void isRiverStale(DBCursor cursor, BSONTimestamp time) throws SlurperException {
        if (cursor == null || time == null) {
            return;
        }
        if (definition.getInitialTimestamp() != null && time.equals(definition.getInitialTimestamp())) {
            return;
        }
        DBObject entry = cursor.next();
        BSONTimestamp oplogTimestamp = (BSONTimestamp) entry.get(MongoDBRiver.OPLOG_TIMESTAMP);
        if (!time.equals(oplogTimestamp)) {
            // IBO
            // MongoDBRiverHelper.setRiverStatus(client, definition.getRiverName(), Status.RIVER_STALE);
            throw new SlurperException("River out of sync with oplog.rs collection");
        }
    }

    private void addQueryToStream(final Operation operation, final BSONTimestamp currentTimestamp, final DBObject update,
                                  final String collection) throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("addQueryToStream - operation [%s], currentTimestamp [%s], update [%s]",
                    operation, currentTimestamp, update));
        }

        if (collection == null) {
            for (String name : slurpedDb.getCollectionNames()) {
                DBCollection slurpedCollection = slurpedDb.getCollection(name);
                for (DBObject item : slurpedCollection.find(update, findKeys)) {
                    addToStream(operation, currentTimestamp, item, collection);
                }
            }
        } else {
            DBCollection slurpedCollection = slurpedDb.getCollection(collection);
            for (DBObject item : slurpedCollection.find(update, findKeys)) {
                addToStream(operation, currentTimestamp, item, collection);
            }
        }
    }

    private String addInsertToStream(final BSONTimestamp currentTimestamp, final DBObject data) throws InterruptedException {
        return addInsertToStream(currentTimestamp, data, definition.getMongoCollection());
    }

    private String addInsertToStream(final BSONTimestamp currentTimestamp, final DBObject data, final String collection)
            throws InterruptedException {
        totalDocuments.incrementAndGet();
        addToStream(Operation.INSERT, currentTimestamp, data, collection);
        return data.containsField(MongoDBRiver.MONGODB_ID_FIELD) ? data.get(MongoDBRiver.MONGODB_ID_FIELD).toString() : null;
    }

    private void addToStream(final Operation operation, final BSONTimestamp currentTimestamp, final DBObject data,
                             final String collection)
            throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("addToStream - operation [%s], currentTimestamp [%s], data [%s], collection [{}]", operation,
                    currentTimestamp,
                    data.get("_id"), collection));
        }
        /* IBO
        logger.info(String.format("addToStream - operation [%s], currentTimestamp [%s], data [%s], collection [{}]", operation,
                currentTimestamp,
                data.get("_id"), collection));
        */
        if (operation == Operation.DROP_DATABASE) {
            if (definition.isImportAllCollections()) {
                for (String name : slurpedDb.getCollectionNames()) {
                    context.getStream().put(new QueueEntry(currentTimestamp, Operation.DROP_COLLECTION, data, name));
                }
            } else {
                context.getStream().put(new QueueEntry(currentTimestamp, Operation.DROP_COLLECTION, data, collection));
            }
        } else {
            context.getStream().put(new QueueEntry(currentTimestamp, operation, data, collection));
        }
    }

    public static class SlurperException extends Exception {
        public SlurperException(String s) {
            super(s);
        }
    }
}
