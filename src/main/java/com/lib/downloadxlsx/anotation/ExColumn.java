package com.lib.downloadxlsx.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Ex column.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExColumn {

  /**
   * Order int.
   *
   * @return the int
   */
  int order();

  /**
   * Column label string.
   *
   * @return the string
   */
  String columnLabel() default "";

  /**
   * Date format string.
   *
   * @return the string
   */
  String dateFormat() default "";

  /**
   * Supply date type string.
   *
   * @return the string
   */
  String supplyDateType() default "";

  /**
   * Number format string.
   *
   * @return the string
   */
  String numberFormat() default "";

  /**
   * Enum output type string.
   *
   * @return the string
   */
  String enumOutputType() default "";

  /**
   * Is only download boolean.
   *
   * @return the boolean
   */
  boolean isOnlyDownload() default false;

  /**
   * Is convert to blank boolean.
   *
   * @return the boolean
   */
  boolean isConvertToBlank() default false;

}
