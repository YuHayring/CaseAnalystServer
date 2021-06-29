package cn.hayring.detecttool.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

/**
 * 地点结点
 */
@NodeEntity
@Data
public class Place extends Node {
    /**
     * neo4j 生成的id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 地点名称
     */
    private String name;


}
