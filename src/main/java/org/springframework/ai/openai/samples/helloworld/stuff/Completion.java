package org.springframework.ai.openai.samples.helloworld.stuff;

public record Completion(String completion) {
}


/**
 * Records were introduced in JDK 16 as a final and immutable data-carrier class in Java. 'Record' is a special kind
 * of class in Java which is used to model plain data aggregates with shallow immutability.
 *
 * In standard classes, a lot of boilerplate code like constructors, getter/setter methods, equals(), hashCode()
 * and toString() methods are required just to store the data. Records help to get rid of this boilerplate by
 * compacting the syntax. In records, these things (constructors, getter/setter methods, equals(), hashCode()
 * and toString()) are automatically created by the compiler.
 *
 * The main intention of a 'Record' is to provide a simple way of creating a class whose primary purpose is to
 * hold data. The data should be open (accessible members), and the representation should be immutable.
 * */