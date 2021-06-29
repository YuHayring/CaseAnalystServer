package cn.hayring.detecttool.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.Date;

/**
 * 案件
 */
@Data
@NodeEntity
public class Case extends Node{

    /**
     * neo4j 生成的id
     */
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Date date;

}
