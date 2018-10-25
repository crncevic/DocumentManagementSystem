@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters({
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type = java.time.LocalDateTime.class, value = com.migesok.jaxb.adapter.javatime.LocalDateTimeXmlAdapter.class),
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type = java.time.LocalDate.class, value = com.migesok.jaxb.adapter.javatime.LocalDateXmlAdapter.class),
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type = java.time.LocalTime.class, value = com.migesok.jaxb.adapter.javatime.LocalTimeXmlAdapter.class)		})

package rs.netset.training;
