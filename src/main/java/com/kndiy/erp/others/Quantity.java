package com.kndiy.erp.others;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Quantity implements Comparable<Quantity>, Serializable {

    private static final RoundingMode DEFAULT_ROUNDINGMODE = RoundingMode.DOWN;
    private static final int DEFAULT_SCALE = 2;
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private BigDecimal quantityValue;
    private String unit;
    private RoundingMode roundingMode;
    private int scale;

    public Quantity(BigDecimal quantityValue, String unit, RoundingMode roundingMode, int scale) {
        this.quantityValue = quantityValue.setScale(scale, roundingMode);;
        this.unit = unit;
        this.roundingMode = roundingMode;
        this.scale = scale;
    }
    public Quantity(BigDecimal quantityValue, String unit) {
        this(quantityValue, unit, DEFAULT_ROUNDINGMODE, DEFAULT_SCALE);
    }

    public <T extends Number> Quantity(T quantityValue, String unit, RoundingMode roundingMode, int scale) {

        this.roundingMode = roundingMode;
        this.scale = scale;
        this.unit = unit;

        if (quantityValue == null) {
            this.quantityValue = new BigDecimal("0").setScale(scale, roundingMode);
        }
        else {
            this.quantityValue = new BigDecimal(quantityValue.toString()).setScale(scale, roundingMode);
        }
    }

    public <T extends Number> Quantity(T quantityValue, String unit) {
        this.roundingMode = DEFAULT_ROUNDINGMODE;
        this.scale = DEFAULT_SCALE;
        this.unit = unit;

        if (quantityValue == null) {
            this.quantityValue = new BigDecimal("0").setScale(scale, roundingMode);
        }
        else {
            this.quantityValue = new BigDecimal(quantityValue.toString()).setScale(scale, roundingMode);
        }

    }

    public Quantity(String quantityValue, String unit) {

        this.roundingMode = DEFAULT_ROUNDINGMODE;
        this.scale = DEFAULT_SCALE;
        this.unit = unit;

        if (quantityValue == null || quantityValue.equals("")) {
            this.quantityValue = new BigDecimal("0").setScale(scale, roundingMode);
        }
        else {
            try {
                this.quantityValue = new BigDecimal(quantityValue.replace(",", "")).setScale(scale, roundingMode);
            }
            catch (Exception ex) {
                throw new NumberFormatException();
            }
        }
    }

    public Quantity(String quantityValue, String unit, RoundingMode roundingMode, int scale) {

        this.roundingMode = roundingMode;
        this.scale = scale;
        this.unit = unit;

        if (quantityValue == null || quantityValue.equals("")) {
            this.quantityValue = new BigDecimal("0");
        }
        else {
            this.quantityValue = new BigDecimal(quantityValue.replace(",", "")).setScale(scale, roundingMode);
        }
    }

    public Quantity(String quantityWithUnit) {

        String[] parsing = quantityWithUnit.split(" ");
        if (parsing.length != 2) {
            throw new IllegalArgumentException("Input String does not contain Unit!");
        }
        else {
            try {
                Integer.parseInt(parsing[1]);
                throw new IllegalArgumentException("Units cannot be numbers!");
            }
            catch (Exception ex) {
            }
        }

        String decimal = parsing[0].split("\\.")[1];

        this.scale = decimal.length();
        this.roundingMode = DEFAULT_ROUNDINGMODE;
        this.unit = parsing[1];

        if (parsing[0].equals("")) {
            this.quantityValue = new BigDecimal("0");
        }
        else {
            this.quantityValue = new BigDecimal(parsing[0].replace(",", "")).setScale(scale, roundingMode);
        }
    }

    public Quantity(String quantityWithUnit, RoundingMode roundingMode, int scale) {

        String[] parsing = quantityWithUnit.split(" ");
        if (parsing.length != 2) {
            throw new IllegalArgumentException("Input String does not contain Unit!");
        }
        else {
            try {
                Integer.parseInt(parsing[1]);
                throw new IllegalArgumentException("Units cannot be numbers!");
            }
            catch (Exception ex) {
            }
        }

        this.scale = scale;
        this.roundingMode = roundingMode;
        this.unit = parsing[1];

        if (parsing[0].equals("")) {
            this.quantityValue = new BigDecimal("0");
        }
        else {
            this.quantityValue = new BigDecimal(parsing[0].replace(",", "")).setScale(scale, roundingMode);
        }
    }

    public boolean isSameUnit(Quantity other) {
        return this.unit.compareTo(other.unit) == 0;
    }

    public void checkUnit(Quantity other) throws MismatchedUnitException {
        if (!isSameUnit(other)) {
            throw new MismatchedUnitException("The two units are different");
        }
    }

    public Quantity plus(Quantity other) throws MismatchedUnitException {
        checkUnit(other);
        return new Quantity(quantityValue.add(other.quantityValue), unit, roundingMode, scale);
    }

    public Quantity minus(Quantity other) throws MismatchedUnitException {
        checkUnit(other);
        return new Quantity(quantityValue.subtract(other.quantityValue), unit, roundingMode, scale);
    }

    public Quantity percentage(Quantity other) throws MismatchedUnitException {
        checkUnit(other);
        return new Quantity(quantityValue.divide(other.quantityValue, roundingMode).multiply(HUNDRED), "%");
    }

    public <T extends Number> Quantity divides(T divisor, RoundingMode roundingModeDivide) {
        return new Quantity(quantityValue.divide(new BigDecimal(divisor.toString()), roundingModeDivide), unit, roundingMode, scale);
    }

    public <T extends Number> Quantity times(T factor) {
        return new Quantity(quantityValue.multiply(new BigDecimal(factor.toString())), unit, roundingMode, scale);
    }

    public Quantity times(Quantity rate) throws MismatchedUnitException {
        String rateDivisorUnit = rate.getUnit().split("/")[1];
        String rateDividendUnit = rate.getUnit().split("/")[0];
        String unit = this.unit;

        if (!rateDividendUnit.equals(unit)) {
            throw new MismatchedUnitException("This is not the correct Exchange Rate Unit!");
        }

        return new Quantity(quantityValue.multiply(new BigDecimal(rate.toString())), rateDivisorUnit, roundingMode, scale);
    }

    public boolean lessThan(Quantity other) {
        return this.compareTo(other) < 0;
    }
    public boolean greaterThan(Quantity other) {
        return this.compareTo(other) > 0;
    }
    public boolean equal(Quantity other) {
        return this.compareTo(other) == 0;
    }
    public BigDecimal getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(BigDecimal quantityValue) {
        this.quantityValue = quantityValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    public void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public int compareTo(Quantity other) {
        return this.quantityValue.compareTo(other.getQuantityValue());
    }

    @Override
    public String toString() {
        return String.format("%,." + scale + "f", quantityValue);
    }
}
