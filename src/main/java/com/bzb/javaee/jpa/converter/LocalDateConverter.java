package com.bzb.javaee.jpa.converter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * A {@link AttributeConverter} that converts {@link LocalDate} to {@link Date} and vice versa.
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(LocalDate dateAttribute) {
    return Optional.ofNullable(dateAttribute).map(Date::valueOf).orElse(null);
  }

  @Override
  public LocalDate convertToEntityAttribute(Date dbDate) {
    return Optional.ofNullable(dbDate).map(Date::toLocalDate).orElse(null);
  }
}
