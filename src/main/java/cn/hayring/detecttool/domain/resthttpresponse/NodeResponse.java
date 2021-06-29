package cn.hayring.detecttool.domain.resthttpresponse;

import cn.hayring.detecttool.domain.Node;
import lombok.Data;

@Data
public class NodeResponse {
    private String label;


    private Node node;


    public NodeResponse(Node node) {
        this.label = node.getLabel();
        this.node = node;
    }
}
