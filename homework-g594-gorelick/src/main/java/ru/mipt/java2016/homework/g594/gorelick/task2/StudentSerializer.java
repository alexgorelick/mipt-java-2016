package ru.mipt.java2016.homework.g594.gorelick.task2;

import java.nio.ByteBuffer;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.function.BooleanSupplier;

import ru.mipt.java2016.homework.tests.task2.Student;

/**
 * Created by alex on 10/31/16.
 */
public class StudentSerializer implements Serializer<Student> {
    @Override
    public ByteBuffer serialize(Student object) {
        IntegerSerializer int_serialize = new IntegerSerializer();
        StringSerializer str_serialize = new StringSerializer();
        DoubleSerializer doub_serialize = new DoubleSerializer();
        ByteBuffer group_id = int_serialize.serialize(object.getGroupId());
        ByteBuffer name = str_serialize.serialize(object.getName());
        ByteBuffer hometown = str_serialize.serialize(object.getHometown());
        DateFormat df = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH);
        ByteBuffer date = str_serialize.serialize(df.format(object.getBiethDate()));
        ByteBuffer has_dormitory = ByteBuffer.allocate(1);
        has_dormitory.put((object.isHasDormitory()) ? 1: 0);
        ByteBuffer average_score = doub_serialize.serialize(object.getAverageScore());
        ByteBuffer result = ByteBuffer.allocate(group_id.capacity() + name.capacity() + hometown.capacity() +
                date.capacity() + has_dormitory.capacity() + average_score.capacity());
        result.put(group_id.array());
        result.put(name.array());
        result.put(hometown.array());
        result.put(date.array());
        result.put(has_dormitory.array());
        return result;
    }
    @Override
    public Student deserialize(ByteBuffer array) {
        IntegerSerializer int_deserialize = new IntegerSerializer();
        StringSerializer str_deserialize = new StringSerializer();
        DoubleSerializer doub_deserialize = new DoubleSerializer();
        Integer group_id = int_deserialize.deserialize(array);
        String name = str_deserialize.deserialize(array);
        String hometown = str_deserialize.deserialize(array);
        DateFormat df = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH);
        String date_string = str_deserialize.deserialize(array);
        Date date = df.parse(date_string);
        Boolean has_dormitory = (array.get() != 0);
        Double average_score = doub_deserialize.deserialize(array);
        return new Student(group_id, name, hometown, date, has_dormitory, average_score);
    }
}