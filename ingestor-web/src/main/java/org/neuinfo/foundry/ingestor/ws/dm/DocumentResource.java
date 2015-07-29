package org.neuinfo.foundry.ingestor.ws.dm;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.wordnik.swagger.annotations.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.neuinfo.foundry.common.model.EntityInfo;
import org.neuinfo.foundry.common.model.Keyword;
import org.neuinfo.foundry.common.util.*;
import org.neuinfo.foundry.ingestor.ws.MongoService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;

/**
 * Created by bozyurt on 7/17/14.
 */
@Path("cinergi/docs")
@Api(value = "cinergi/docs", description = "Metadata Documents")
public class DocumentResource {
    private static String theApiKey = "72b6afb31ba46a4e797c3f861c5a945f78dfaa81";

    @Path("/{resourceId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "An internal error occurred during the retrieval of document ids")})
    @ApiOperation(value = "Retrieve all document ids for the given resource ID",
            notes = "",
            response = String.class)
    public Response getDocumentIdsForResource(@ApiParam(value = "The resource ID for the harvest source", required = true) @PathParam("resourceId") String resourceId) {
        try {
            MongoService mongoService = MongoService.getInstance();

            Set<String> statusSet = new HashSet<>(7);
            statusSet.add("finished");
            JSONArray documentIds4Source = mongoService.findDocumentIds4Source(resourceId, statusSet);
            JSONObject json = new JSONObject();
            json.put("documentIds", documentIds4Source);
            String jsonStr = json.toString(2);
            return Response.ok(jsonStr).build();
        } catch (Exception x) {
            x.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/{resourceId}/{docId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "An internal error occurred during the document retrieval"),
            @ApiResponse(code = 404, message = "No metadata document is found with the given resource ID and document ID")})
    @ApiOperation(value = "Retrieve the original document as XML",
            notes = "",
            response = String.class)
    public Response getDocument(@ApiParam(value = "The resource ID for the harvest source", required = true) @PathParam("resourceId") String resourceId,
                                @ApiParam(value = "The document ID for the metadata document", required = true) @PathParam("docId") String docId) {
        String xmlStr = null;
        try {
            final MongoService mongoService = MongoService.getInstance();

            BasicDBObject docWrapper = mongoService.findTheDocument(resourceId, docId);
            if (docWrapper == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No document with id:" + docId + " is found in the source " + resourceId).build();
            }

            ISOXMLGenerator generator = new ISOXMLGenerator();

            Element docEl = generator.generate(docWrapper);
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

            Document doc = new Document();
            doc.setRootElement(docEl);
            StringWriter sw = new StringWriter(16000);
            xmlOutputter.output(doc, sw);

            xmlStr = sw.toString();
        } catch (Exception x) {
            x.printStackTrace();
        }
        return Response.ok(xmlStr).build();
    }

    @Path("/keyword/hierarchies/")
    @GET
    @Produces("application/json")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "An internal error occurred during the keyword hierarchy processing"),
            @ApiResponse(code = 404, message = "No metadata document is found with the given document ID")})
    @ApiOperation(value = "Retrieve keywords and their ontology hierarchies for a ISO metadata document",
            notes = "",
            response = String.class)
    public Response getKeywordHierarchies(
            @ApiParam(value = "The document ID for the metadata document", required = true) @QueryParam("id") String docId) {
        String jsonStr = null;
        try {
            final MongoService mongoService = MongoService.getInstance();
            System.out.println("docId:" + docId);
            BasicDBObject docWrapper = mongoService.findTheDocument(docId);
            if (docWrapper == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No document with id:" + docId + " is not found!").build();
            }
            KeywordHierarchyHandler handler =  KeywordHierarchyHandler.getInstance();
            JSONObject result = new JSONObject();
            BasicDBObject data = (BasicDBObject) docWrapper.get("Data");
            BasicDBList keywords = (BasicDBList) data.get("keywords");
            JSONArray keywordsArr = new JSONArray();
            result.put("keywords", keywordsArr);
            //IHierarchyHandler chh = CategoryHierarchyHandler.getInstance();
            IHierarchyHandler chh = FacetHierarchyHandler.getInstance();
            if (keywords != null && !keywords.isEmpty()) {
                for (Object o : keywords) {
                    JSONObject keywordJson = JSONUtils.toJSON((BasicDBObject) o, true);
                    JSONObject kwhJson = prepHierarchyForKeyword(keywordJson, handler, chh);
                    if (kwhJson.has("hierarchy")) {
                        keywordsArr.put(kwhJson);
                    }
                }
            }
            // also add existing keywords
            BasicDBObject origDocDBO = (BasicDBObject) docWrapper.get("OriginalDoc");
            JSONObject origDocJson = JSONUtils.toJSON(origDocDBO, false);
            XML2JSONConverter converter = new XML2JSONConverter();
            Element docEl = converter.toXML(origDocJson);
            // Utils.saveXML(docEl, "/tmp/xpath_test.xml");


            List<JSONObject> jsonObjects = prepHierarchies(docEl, handler, chh);
            for (JSONObject js : jsonObjects) {
                keywordsArr.put(js);
            }


            jsonStr = result.toString(2);
        } catch (Exception x) {
            x.printStackTrace();
            return Response.serverError().build();
        }

        return Response.ok(jsonStr).build();
    }

    List<JSONObject> prepHierarchies(Element docEl, KeywordHierarchyHandler handler,
                                     IHierarchyHandler chh) {
        List<JSONObject> jsonList = new ArrayList<JSONObject>(5);
        Map<String, Namespace> nsMap = new HashMap<String, Namespace>();
        List<Namespace> namespacesInScope = docEl.getNamespacesInScope();
        for (Namespace ns : namespacesInScope) {
            nsMap.put(ns.getPrefix(), ns);
        }
        XPathFactory xpathFactory = XPathFactory.instance();

        Namespace ns = nsMap.get("gmd");
        Assertion.assertNotNull(ns);
        XPathExpression<Element> expr = xpathFactory.compile("//gmd:MD_Keywords",
                Filters.element(), null, ns);
        Document doc = new Document(docEl);
        List<Element> elements = expr.evaluate(doc);
        if (elements != null && !elements.isEmpty()) {

            Namespace gcoNS = nsMap.get("gco");
            for (Element el : elements) {
                List<Element> children = el.getChildren("keyword", ns);
                for (Element child : children) {
                    String keywordStr = child.getChildTextTrim("CharacterString", gcoNS);
                    Map<String, Keyword> kwMap = new HashMap<String, Keyword>(7);
                    try {
                        handler.annotateEntities(null, keywordStr, kwMap);
                        if (!kwMap.isEmpty()) {
                            Keyword theKW = null;
                            int maxPathLen = -10;
                            for (Keyword kw : kwMap.values()) {
                                if (kw.hasCategory()) {
                                    for (String category : kw.getCategories()) {
                                        String categoryPath = chh.getCinergiCategory(category.toLowerCase());
                                        int pathLength2Root = chh.getPathLength2Root(categoryPath);
                                        if (pathLength2Root > maxPathLen) {
                                            maxPathLen = pathLength2Root;
                                            theKW = kw;
                                        }
                                    }
                                }
                            }
                            if (theKW != null) {
                                EntityInfo ei = theKW.getTheCategoryEntityInfo(chh);
                                if (ei != null && !Utils.isEmpty(ei.getId())) {
                                    String ontologyID = getOntologyID(ei.getId());
                                    String keywordHierarchy = handler.getKeywordHierarchy(keywordStr, ontologyID);
                                    if (keywordHierarchy != null && keywordHierarchy.length() > 0) {

                                        JSONObject json = new JSONObject();
                                        json.put("keyword", keywordStr);
                                        json.put("hierarchy", keywordHierarchy);
                                        jsonList.add(json);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return jsonList;
    }


    JSONObject prepHierarchyForKeyword(JSONObject keywordJson, KeywordHierarchyHandler handler,
                                       IHierarchyHandler chh) {
        JSONObject json = new JSONObject();
        Keyword keyword = Keyword.fromJSON(keywordJson);
        EntityInfo ei = keyword.getTheCategoryEntityInfo(chh);
        if (ei != null && !Utils.isEmpty(ei.getId())) {
            String ontologyID = getOntologyID(ei.getId());
            try {
                String keywordHierarchy = handler.getKeywordHierarchy(keyword.getTerm(), ontologyID);
                if (keywordHierarchy != null && keywordHierarchy.length() > 0) {
                    json.put("keyword", keyword.getTerm());
                    json.put("hierarchy", keywordHierarchy);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Map<String, Keyword> kwMap = new HashMap<String, Keyword>(7);
            try {
                handler.annotateEntities(null, keyword.getTerm(), kwMap);
                if (!kwMap.isEmpty()) {
                    Keyword kw = kwMap.values().iterator().next();
                    ei = kw.getTheCategoryEntityInfo(chh);
                    if (ei != null && !Utils.isEmpty(ei.getId())) {
                        String ontologyID = getOntologyID(ei.getId());
                        String keywordHierarchy = handler.getKeywordHierarchy(keyword.getTerm(), ontologyID);
                        if (keywordHierarchy != null && keywordHierarchy.length() > 0) {
                            json.put("keyword", keyword.getTerm());
                            json.put("hierarchy", keywordHierarchy);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public static String getOntologyID(String id) {
        int idx = id.lastIndexOf('/');
        if (idx != -1) {
            return id.substring(idx + 1);
        }
        return id;
    }


    //    /**
//     * @param nifId        NIF ID for the source
//     * @param docId        Document ID
//     * @param batchId      batchId for the ingestion set in the format of <code>YYYYMMDD</code>
//     * @param in           The xml file to ingest
//     * @param ingestStatus One of <code>start</code> or <code>end</code>. Used to indicate the start and end of group of documents ingested in a batch.
//     * @return
//     */
/*
    @Path("docs/{nifId}/{docId}")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveDocument(@PathParam("nifId") String nifId,
                                 @PathParam("docId") String docId,
                                 @FormDataParam("batchId") String batchId,
                                 @FormDataParam("file") InputStream in,
                                 @FormDataParam("ingestStatus") String ingestStatus,
                                 @FormDataParam("apiKey") String apiKey

    ) {
        if (batchId == null || ingestStatus == null || apiKey == null) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());
        }
        System.out.println("batchId:" + batchId);
        System.out.println("nifId:" + nifId);
        if (apiKey == null || !apiKey.equals(theApiKey)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        String dataSource = "";
        MongoService mongoService = null;
        try {
            String xmlStr = toXMLString(in);
            mongoService = MongoService.getInstance();
            if (mongoService.hasDocument(nifId, docId)) {
                return Response.status(Response.Status.FOUND).
                        entity("A document with id " + docId +
                                " for source " + nifId + " already exists!").build();
            }

            final Source source = mongoService.findSource(nifId);
            if (source == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No source with the nifId:" + nifId).build();
            }
            final List<BatchInfo> batchInfos = source.getBatchInfos();


            if (batchInfos.isEmpty() && ingestStatus.equals("in_process")) {
                return Response.serverError().build();
            } else {
                //TODO
            }
            XML2JSONConverter converter = new XML2JSONConverter();
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(xmlStr));
            Element rootEl = doc.getRootElement();

            final JSONObject json = converter.toJSON(rootEl);

            mongoService.saveDocument(json, batchId, source.getResourceID(), source.getName(), true,
                    source, docId);


            if (ingestStatus.equals("start")) {
                mongoService.beginBatch(nifId, dataSource, batchId, true);
            } else if (ingestStatus.equals("in_process")) {
                mongoService.updateBatch(nifId, dataSource, batchId, true);
            } else if (ingestStatus.equals("end")) {
                mongoService.updateBatch(nifId, dataSource, batchId, true);
                mongoService.endBatch(nifId, "", batchId);
            }
            String jsonStr = json.toString(2);
            // System.out.println("json:\n" + jsonStr);
            // System.out.println("=============================");


            return Response.ok(jsonStr).build();
        } catch (Exception e) {
            e.printStackTrace();
            if (mongoService != null) {
                if (ingestStatus.equals("start")) {
                    mongoService.beginBatch(nifId, dataSource, batchId, false);
                } else {
                    mongoService.updateBatch(nifId, dataSource, batchId, false);
                }
            }
            return Response.serverError().build();
        }

        // return Response.ok("{}").build();

    }
*/
    private String toXMLString(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder(16000);
        BufferedReader bin;
        try {
            bin = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bin.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } finally {
            Utils.close(in);
        }
    }

}
