db.sources.ensureIndex({"sourceInformation.resourceID":1},{unique:1});
db.sources.ensureIndex({"sourceInformation.name":1},{unique:1});

db.records.ensureIndex({"SourceInfo.SourceID":1});
db.records.ensureIndex({"primaryKey":1},{unique:1});
