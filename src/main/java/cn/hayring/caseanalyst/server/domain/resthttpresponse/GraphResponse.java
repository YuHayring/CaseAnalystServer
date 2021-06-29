package cn.hayring.caseanalyst.server.domain.resthttpresponse;

import cn.hayring.caseanalyst.server.domain.Relationship;
import lombok.Data;

import java.util.List;

@Data
public class GraphResponse {
    List<List> nodes;

    List<Relationship> relationships;


    public GraphResponse(List<List> nodes, List<Relationship> relationships) {
        this.nodes = nodes;
        this.relationships = relationships;
    }
}
