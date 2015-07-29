package org.neuinfo.foundry.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.neuinfo.foundry.common.model.EntityInfo;
import org.neuinfo.foundry.common.model.Keyword;

import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by bozyurt on 4/20/15.
 */
public class KeywordHierarchyHandler {
    String serviceURL = "http://tikki.neuinfo.org:9000/";
    Map<String, String> lruCache = new LRUCache<String, String>(100);
    Map<String, List<String>> parentKeywordsCache = new LRUCache<String, List<String>>(1000);
    private static KeywordHierarchyHandler instance = null;

    public static KeywordHierarchyHandler getInstance() {
        if (instance == null) {
            instance = new KeywordHierarchyHandler();
        }
        return instance;
    }

    private KeywordHierarchyHandler() {
    }

    public synchronized List<String> getParentKeywords(String keyword, String id) throws Exception {
        List<String> cachedParentKeywords = parentKeywordsCache.get(id);
        if (cachedParentKeywords != null) {
            return cachedParentKeywords;
        }
        HttpClient client = new DefaultHttpClient();
        URIBuilder builder = new URIBuilder(this.serviceURL);
        //builder.setPath("/scigraph/graph/neighbors/" + id + ".json");
        builder.setPath("/scigraph/graph/neighbors");
        builder.setParameter("id", id);
        builder.setParameter("depth", "100");
        builder.setParameter("blankNodes", "false");
        builder.setParameter("relationshipType", "subClassOf");
        builder.setParameter("direction", "OUTGOING");
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        System.out.println("uri:" + uri);
        httpGet.addHeader("Accept", "application/json");
        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
                String jsonStr = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonStr);
                // System.out.println(json.toString(2));
                // System.out.println("================");

                String hierarchy = prepHierarchy(json, keyword, id);
                System.out.println("hierarchy: " + hierarchy);
                String[] tokens = hierarchy.split(" > ");
                cachedParentKeywords = new ArrayList<String>(tokens.length - 1);
                for (String token : tokens) {
                    token = token.trim();
                    if (!token.equals(keyword)) {
                        cachedParentKeywords.add(token);
                    }
                }
                parentKeywordsCache.put(id, cachedParentKeywords);
                return cachedParentKeywords;
            }
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return null;
    }

    public synchronized String getKeywordFacetHierarchy(String keyword, String id) throws Exception {
        String cachedHierarchy = lruCache.get(id);
        if (cachedHierarchy != null) {
            return cachedHierarchy;
        }
//http://tikki.neuinfo.org:9000/scigraph/dynamic/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FENVO_00002871/facets
        HttpClient client = new DefaultHttpClient();
        URIBuilder builder = new URIBuilder(this.serviceURL);
        String path = "scigraph/dynamic/" + URLEncoder.encode(id, "UTF-8") + "/facets";
        System.out.println(path);
        URI uri = new URI(this.serviceURL + path);
        HttpGet httpGet = new HttpGet(uri);
        System.out.println("uri:" + uri);
        httpGet.addHeader("Accept", "application/json");
        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
                String jsonStr = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonStr);
                //  System.out.println(json.toString(2));
                //  System.out.println("================");

                String hierarchy = prepHierarchy(json, keyword, id);
                System.out.println("hierarchy: " + hierarchy);
                lruCache.put(id, hierarchy);
                return hierarchy;
            }

        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return null;
    }

    // tikki.neuinfo.org:9000/scigraph/graph/neighbors/Manometer.json?depth=100&blankNodes=false&relationshipType=subClassOf&direction=out
    public synchronized String getKeywordHierarchy(String keyword, String id) throws Exception {
        String cachedHierarchy = lruCache.get(id);
        if (cachedHierarchy != null) {
            return cachedHierarchy;
        }

        HttpClient client = new DefaultHttpClient();
        URIBuilder builder = new URIBuilder(this.serviceURL);
        builder.setPath("/scigraph/graph/neighbors/" + id + ".json");
        // builder.setPath("/scigraph/graph/neighbors");
        //builder.setParameter("id", id);
        builder.setParameter("depth", "100");
        builder.setParameter("blankNodes", "false");
        builder.setParameter("relationshipType", "subClassOf");
        builder.setParameter("direction", "OUTGOING");
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        System.out.println("uri:" + uri);
        httpGet.addHeader("Accept", "application/json");
        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
                String jsonStr = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonStr);
                //System.out.println(json.toString(2));
                // System.out.println("================");

                String hierarchy = prepHierarchy(json, keyword, id);
                System.out.println("hierarchy: " + hierarchy);
                lruCache.put(id, hierarchy);
                return hierarchy;
            }
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return null;
    }


    String prepHierarchy(JSONObject json, String keyword, String id) {
        Map<String, Node> nodeMap = new HashMap<String, Node>(7);
        Map<String, Edge> edgeMap = new HashMap<String, Edge>(7);
        List<Edge> edges = new ArrayList<Edge>(10);
        JSONArray nodesArr = json.getJSONArray("nodes");
        for (int i = 0; i < nodesArr.length(); i++) {
            Node node = Node.fromJSON(nodesArr.getJSONObject(i));
            nodeMap.put(node.id, node);
        }
        JSONArray edgesArr = json.getJSONArray("edges");
        Edge startEdge = null;
        for (int i = 0; i < edgesArr.length(); i++) {
            Edge edge = Edge.fromJSON(edgesArr.getJSONObject(i));
            if (edge.sub.endsWith(id)) {
                startEdge = edge;
            }
            edgeMap.put(edge.sub, edge);
            edges.add(edge);
        }
        Assertion.assertNotNull(startEdge);
        StringBuilder sb = new StringBuilder();
        Node n = nodeMap.get(startEdge.sub);
        List<String> path = new ArrayList<String>(edgeMap.size());
        while (n != null) {
            String pathPart = n.label;
            if (n.label == null || n.label.length() == 0) {
                pathPart = n.id;
                int hashIdx = pathPart.lastIndexOf('#');
                if (hashIdx != -1) {
                    pathPart = pathPart.substring(hashIdx + 1);
                }
            }

            path.add(pathPart);
            Edge e = edgeMap.get(startEdge.obj);
            if (e == null) {
                break;
            }
            n = nodeMap.get(e.sub);
            startEdge = e;
        }
        for (Iterator<String> it = path.iterator(); it.hasNext(); ) {
            String pathPart = it.next();
            sb.append(pathPart);
            if (it.hasNext()) {
                sb.append(" > ");
            }
        }

        return sb.toString().trim();
    }

    public void annotateEntities(String contentLocation, String text, Map<String, Keyword> keywordMap) throws Exception {
        HttpClient client = new DefaultHttpClient();
        String url = "http://tikki.neuinfo.org:9000/scigraph/annotations/entities";
        URIBuilder builder = new URIBuilder(url);
        builder.setParameter("content", text);
        // minLength=4&longestOnly=true&includeAbbrev=false&includeAcronym=false&includeNumbers=false&callback=fn
        builder.setParameter("minLength", "4");
        builder.setParameter("longestOnly", "true");
        builder.setParameter("includeAbbrev", "false");
        builder.setParameter("includeNumbers", "false");

        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        //System.out.println("uri:" + uri);
        // httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Accept", "application/json");
        try {
            final HttpResponse response = client.execute(httpGet);
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonStr = EntityUtils.toString(entity);
                try {
                    //System.out.println(new JSONArray(jsonStr).toString(2));
                    //System.out.println("================");
                    JSONArray jsArr = new JSONArray(jsonStr);
                    String textLC = text.toLowerCase();
                    for (int i = 0; i < jsArr.length(); i++) {
                        final JSONObject json = jsArr.getJSONObject(i);
                        if (json.has("token")) {
                            JSONObject tokenObj = json.getJSONObject("token");
                            String id = tokenObj.getString("id");
                            final JSONArray terms = tokenObj.getJSONArray("terms");
                            if (terms.length() > 0) {
                                int start = json.getInt("start");
                                int end = json.getInt("end");
                                String term = findMatchingTerm(terms, textLC);
                                if (term != null) {
                                    Keyword keyword = keywordMap.get(term);
                                    if (keyword == null) {
                                        keyword = new Keyword(term);
                                        keywordMap.put(term, keyword);
                                    }
                                    JSONArray categories = tokenObj.getJSONArray("categories");
                                    String category = "";
                                    if (categories.length() > 0) {
                                        category = categories.getString(0);
                                    }
                                    EntityInfo ei = new EntityInfo(contentLocation, id, start, end, category);
                                    keyword.addEntityInfo(ei);
                                }
                            }
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    public static String findMatchingTerm(JSONArray jsArr, String text) {
        for (int i = 0; i < jsArr.length(); i++) {
            String term = jsArr.getString(i);
            if (text.indexOf(term.toLowerCase()) != -1) {
                return term;
            }
        }
        // if no match has found return the first term
        for (int i = 0; i < jsArr.length(); i++) {
            String term = jsArr.getString(i);
            if (term != null && term.length() > 0) {
                return term;
            }
        }
        return null;
    }

    public static class Node {
        String id;
        String label;
        List<String> categories = new ArrayList<String>(2);
        Node parent;
        List<Node> children;

        public Node(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public List<Node> getChildren() {
            return children;
        }

        public void addChild(Node child) {
            if (this.children == null) {
                this.children = new LinkedList<Node>();
            }
            children.add(child);
        }

        public boolean hasChildren() {
            return children != null && !children.isEmpty();
        }

        public static Node fromJSON(JSONObject json) {
            String id = json.getString("id");
            String label = null;
            if (json.has("lbl")) {
                Object o = json.get("lbl");
                if (o instanceof String) {
                    label = (String) o;
                }
            }
            Node node = new Node(id, label);
            if (json.has("meta")) {
                JSONObject metaJS = json.getJSONObject("meta");
                if (metaJS.has("category")) {
                    JSONArray jsArr = metaJS.getJSONArray("category");
                    for (int i = 0; i < jsArr.length(); i++) {
                        node.categories.add(jsArr.getString(i));
                    }
                }
                String cinergiPrefLabel = "http://hydro10.sdsc.edu/cinergi_ontology/cinergiExtensions.owl#cinergiPreferredLabel";
                if (metaJS.has(cinergiPrefLabel)) {
                    JSONArray jsArr = metaJS.getJSONArray(cinergiPrefLabel);
                    if (jsArr.length() > 0 && jsArr.getString(0).length() > 0) {
                        node.label = jsArr.getString(0);
                    }
                }

            }
            return node;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Node{");
            sb.append("id='").append(id).append('\'');
            sb.append(", label='").append(label).append('\'');
            sb.append(", categories=").append(categories);
            if (parent != null) {
                sb.append(", parent=").append(parent.id);
            }
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Edge {
        String sub;
        String obj;
        String pred;

        public Edge(String sub, String obj, String pred) {
            this.sub = sub;
            this.obj = obj;
            this.pred = pred;
        }

        public static Edge fromJSON(JSONObject json) {
            String sub = json.getString("sub");
            String obj = json.getString("obj");
            String pred = json.getString("pred");

            return new Edge(sub, obj, pred);
        }
    }

    public static void main(String[] args) throws Exception {
        KeywordHierarchyHandler handler = KeywordHierarchyHandler.getInstance();

//        handler.getKeywordHierarchy("Manometer", "Manometer");
        // handler.getKeywordHierarchy("mercury","b0e515cf-ed97-4870-bdde-6c00b0c998ee");
//        handler.getKeywordHierarchy("mountain", "ENVO_00000081");

        handler.getKeywordFacetHierarchy("Proteomics", "http://purl.obolibrary.org/obo/ENVO_00002871");

    }
}
