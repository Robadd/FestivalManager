package de.robadd.festivalmanager.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

import java.time.LocalDateTime;

import org.assertj.core.api.Condition;
import org.testng.annotations.Test;

import de.robadd.festivalmanager.model.annotation.DbField;
import de.robadd.festivalmanager.model.annotation.DbTable;
import de.robadd.festivalmanager.model.annotation.Id;

public class TableMetaDataTest
{
    @Test
    public void fromTest() throws Exception
    {
        TableMetaData<TestClass> testMeta = TableMetaData.from(TestClass.class);
        assertThat(testMeta).extracting(TableMetaData::getClazz).isEqualTo(TestClass.class);
        assertThat(testMeta).extracting(TableMetaData::getIdField).isEqualTo("id");
        assertThat(testMeta.getTableName()).isEqualTo("test");
        assertThat(testMeta).extracting(TableMetaData::getFieldsMeta).asList().hasSize(5);
        assertThat(testMeta.getFieldsMeta())
                .haveExactly(2, new Condition<>(a -> a.getClazz().equals(Integer.class), ""))
                .haveExactly(1, new Condition<>(a -> a.getClazz().equals(String.class), ""))
                .haveExactly(1, new Condition<>(a -> a.getClazz().equals(Boolean.class), ""))
                .haveExactly(1, new Condition<>(a -> a.getClazz().equals(LocalDateTime.class), ""));

        assertThat(testMeta.getFieldsMeta())
                .haveExactly(1, new Condition<>(a -> a.getColumnName().equals("id"), "id"))
                .haveExactly(1, new Condition<>(a -> a.getColumnName().equals("integer"), "integer"))
                .haveExactly(1, new Condition<>(a -> a.getColumnName().equals("string"), "string"))
                .haveExactly(1, new Condition<>(a -> a.getColumnName().equals("boolean"), "boolean"))
                .haveExactly(1, new Condition<>(a -> a.getColumnName().equals("localDateTime"), "localDateTime"));

        TestClass.class.getMethod("getInteger");
        TestClass.class.getMethod("setInteger", Integer.class);

        assertThat(testMeta.getFieldsMeta().stream().filter(a -> a.getColumnName().equals("integer")).findFirst())
                .isPresent().get().returns(TestClass.class.getMethod("getInteger"), from(DbMetaData::getGetter))
                .returns(TestClass.class.getMethod("setInteger", Integer.class), from(DbMetaData::getSetter))
                .returns(TestClass.class.getDeclaredField("integer"), from(DbMetaData::getField));

        TableMetaData<EmptyClass> empty = TableMetaData.from(EmptyClass.class);
        assertThat(empty).isNull();
    }

    class EmptyClass
    {

    }

    @DbTable("test")
    class TestClass
    {
        @Id
        @DbField("id")
        private Integer id;
        @DbField("integer")
        private Integer integer;
        @DbField("string")
        private String string;
        @DbField("boolean")
        private Boolean bool;
        @DbField("localDateTime")
        private LocalDateTime localDateTime;
        @DbField("noSetterOrGetter")
        public Integer noSetterOrGetter;

        private String notSerialized;

        /**
         * @return the id
         */
        public Integer getId()
        {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(final Integer id)
        {
            this.id = id;
        }

        /**
         * @return the integer
         */
        public Integer getInteger()
        {
            return integer;
        }

        /**
         * @param integer the integer to set
         */
        public void setInteger(final Integer integer)
        {
            this.integer = integer;
        }

        /**
         * @return the string
         */
        public String getString()
        {
            return string;
        }

        /**
         * @param string the string to set
         */
        public void setString(final String string)
        {
            this.string = string;
        }

        /**
         * @return the bool
         */
        public Boolean getBool()
        {
            return bool;
        }

        /**
         * @param bool the bool to set
         */
        public void setBool(final Boolean bool)
        {
            this.bool = bool;
        }

        /**
         * @return the localDateTime
         */
        public LocalDateTime getLocalDateTime()
        {
            return localDateTime;
        }

        /**
         * @param localDateTime the localDateTime to set
         */
        public void setLocalDateTime(final LocalDateTime localDateTime)
        {
            this.localDateTime = localDateTime;
        }

        /**
         * @return the notSerialized
         */
        public String getNotSerialized()
        {
            return notSerialized;
        }

        /**
         * @param notSerialized the notSerialized to set
         */
        public void setNotSerialized(final String notSerialized)
        {
            this.notSerialized = notSerialized;
        }

    }
}
