package de.robadd.festivalmanager.model.type;

public interface JSONWritable
{
    void fillFromJson(String line);

    String toJson();
}
