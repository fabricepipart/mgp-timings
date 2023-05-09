package org.teknichrono.mgp.csv.converter;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.StringUtils;

class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
  @Override
  public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
    final int numColumns = getFieldMap().values().size();
    super.generateHeader(bean);

    String[] header = new String[numColumns];

    BeanField<T, Integer> beanField;
    for (int i = 0; i < numColumns; i++) {
      beanField = findField(i);
      String columnHeaderName = extractHeaderName(beanField);
      header[i] = columnHeaderName;
    }
    return header;
  }

  private String extractHeaderName(final BeanField<T, Integer> beanField) {
    if (beanField == null || beanField.getField() == null || beanField.getField().getDeclaredAnnotationsByType(
        CsvBindByName.class).length == 0) {
      return StringUtils.EMPTY;
    }

    final CsvBindByName bindByNameAnnotation = beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0];
    return bindByNameAnnotation.column();
  }
}
