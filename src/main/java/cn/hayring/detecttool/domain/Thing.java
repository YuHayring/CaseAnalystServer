package cn.hayring.detecttool.domain;

import lombok.Data;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

@NodeEntity
@Data
public class Thing extends Node {

    /**
     * neo4j 生成的id
     */
    @Id
    @GeneratedValue
    private Long id;

    private String brand;

    private String name;


    public Thing() {

    }


    @Override
    public String getName() {
        return name;
    }
}
