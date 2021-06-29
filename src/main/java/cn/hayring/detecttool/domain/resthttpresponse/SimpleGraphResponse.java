package cn.hayring.detecttool.domain.resthttpresponse;

import cn.hayring.detecttool.domain.Relationship;

import java.util.*;

public class SimpleGraphResponse {

    Collection<Out> nodes;

    Collection<Out> edges;




    static class Out{
        Object data;

        public Out(Object data) {
            this.data = data;
        }
    }


    static class Edge {
        String source;
        String target;
        String relationship;

        public Edge(String source, String target, String relationship) {
            this.source = source;
            this.target = target;
            this.relationship = relationship;
        }
    }

    static class Node {
        String id;
        String name;
        String label;

        public Node(cn.hayring.detecttool.domain.Node node) {
            this.id = node.getId().toString();
            this.name = node.getName();
            this.label = node.getClass().getSimpleName();
        }
    }

    public SimpleGraphResponse(List<Relationship> relationships) {
//        Set<Long> added = new HashSet<>();
//        nodes = new ArrayList<>();
//        edges = new ArrayList<>();
//        for (Relationship relationship : relationships) {
//            cn.hayring.detecttool.domain.Node start = relationship.getStart();
//            cn.hayring.detecttool.domain.Node end = relationship.getEnd();
//            if (!added.contains(start.getId())) {
//                nodes.add(new Out(new Node(relationship.getStart())));
//                added.add(start.getId());
//            }
//            if (!added.contains(end.getId())) {
//                nodes.add(new Out(new Node(relationship.getEnd())));
//                added.add(end.getId());
//            }
//            edges.add(new Out(new Edge(relationship.getStart().getId().toString(),
//                    relationship.getEnd().getId().toString(),relationship.getType())));
//        }
    }
}
