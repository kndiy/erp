class BigDecimal {

    // Configuration: constants
    static DECIMALS = 18; // number of decimals on all instances
    static ROUNDED = true; // numbers are truncated (false) or rounded (true)
    static SHIFT = BigInt("1" + "0".repeat(BigDecimal.DECIMALS)); // derived constant
    
    constructor(value) {
        if (value instanceof BigDecimal) return value;
        //concat("") to turn Number into String
        let [integral, decimal] = String(value).split(".").concat("");
        //_n should be a way to call a hidden intrinsic variable without declaring it first
        //not what I would do with my API class
        this._n = BigInt(integral
                            //decimal length can be longer than 18 (maximum for our class config), in this case padding will do nothing
                            //so there is a need to slice (take only 18 characters) (0 - 17 places)
                            + decimal.padEnd(BigDecimal.DECIMALS, "0").slice(0, BigDecimal.DECIMALS))
                            //if rounded == true AND decimal.charAt(18) (outside 0-17 range) >= 5 then plus 1 to the former part
                            + BigInt(BigDecimal.ROUNDED && decimal[BigDecimal.DECIMALS] >= "5");
    }

    static fromBigInt(bigint) {
        //Object.assign(target, source1, source2, source3, ...)
        //literally create an empty prototype of BigDecimal, then assign bigint (argument value) to field: _n
        return Object.assign(Object.create(BigDecimal.prototype), { _n: bigint });
    }

    add(num) {
        return BigDecimal.fromBigInt(this._n + new BigDecimal(num)._n);
    }

    subtract(num) {
        return BigDecimal.fromBigInt(this._n - new BigDecimal(num)._n);
    }

    static _divRound(dividend, divisor) {
        return BigDecimal.fromBigInt(dividend / divisor
            + (BigDecimal.ROUNDED ? dividend  * 2n / divisor % 2n : 0n));
    }

    multiply(num) {
        return BigDecimal._divRound(this._n * new BigDecimal(num)._n, BigDecimal.SHIFT);
    }

    divide(num) {
        return BigDecimal._divRound(this._n * BigDecimal.SHIFT, new BigDecimal(num)._n);
    }

    //this is actually the most important thing of this class
    //without it, a BigDecimal is just a BigInt without any Integral of Decimal parts whatsoever
    toString() {
        const s = this._n.toString().padStart(BigDecimal.DECIMALS+1, "0");
        return s.slice(0, -BigDecimal.DECIMALS) + "." + s.slice(-BigDecimal.DECIMALS)
                .replace(/\.?0+$/, "");
    }
}