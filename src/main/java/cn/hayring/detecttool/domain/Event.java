package cn.hayring.detecttool.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

/**
 * 事件 结点
 */
@NodeEntity
@Data
public class Event extends Node {

    /**
     * neo4j 生成的id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 事件名称
     */
    private String name;

}
