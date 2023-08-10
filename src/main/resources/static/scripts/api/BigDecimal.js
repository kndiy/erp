class BigDecimal {

    static SCALE = 2;
    static ROUNDING = true;

    constructor(value, rounding, scale) {
        if (value instanceof BigDecimal) {
            return value;
        }

        this.scale = scale;
        this.rounding = rounding;
        this.shift = BigInt("1" + "0".repeat(scale));

        let [integral, decimal] = value.toString().replace(",", "").split(".").concat("");

        this.value = BigInt(integral
                                + decimal.padEnd(this.scale, "0").slice(0, this.scale))
                                + BigInt(rounding && decimal[this.scale] >= "5");
    }

    plus(number) {
        let cal = this.value + new BigDecimal(number, this.rounding, this.scale).value;
        let res = new BigDecimal(0, this.rounding, this.scale);
        res.value = cal;
        return res;
    }

    minus(number) {
        let cal = this.value - new BigDecimal(number, this.rounding, this.scale).value;
        let res = new BigDecimal(0, this.rounding, this.scale);
        res.value = cal;
        return res;
    }

    times(number) {
        let cal = this.value * new BigDecimal(number, this.rounding, this.scale).value / this.shift;
        let res = new BigDecimal(0, this.rounding, this.scale);
        res.value = cal;
        return res;
    }

    divides(number) {
        let cal = this.value  * this.shift / new BigDecimal(number, this.rounding, this.scale).value;
        let res = new BigDecimal(0, this.rounding, this.scale);
        res.value = cal;
        return res;
    }

    toString() {
        let stringValue = this.value.toString();
        let length = stringValue.length;

        let decimalLength = this.scale;
        let integralLength = length - decimalLength;

        let integral = stringValue.slice(0, integralLength).concat("");
        let decimal = stringValue.slice(integralLength, length).concat("");

        return (integral + "." + decimal) == ".0" ? ("0." + "0".repeat(this.scale)) : (integral.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "." + decimal);
    }

}