package cn.hayring.detecttool.domain;

import cn.hayring.detecttool.domain.Node;
import cn.hayring.detecttool.domain.Person;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.springframework.data.neo4j.annotation.QueryResult;

@Data
@QueryResult
public class Relationship {


    private Long id;

    private Long start;

    private Long end;

    private String type;

    private Integer weight;

    private String info;



    public static final String HAPPENED_IN = "HAPPENED_IN";

    public static final String P2P = "P2P";

    public static final String P2T = "P2T";

    public static final String TAKE_PART = "TAKE_PART";

    public static final String CAUSE = "CAUSE";

    public static final String APPEAR_AT = "APPEAR_AT";

    public static final String P2PLACE = "P2PLACE";

    public static final String NEAR = "NEAR";

    public static final String USE = "USE";


}
