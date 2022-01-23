let AppGlobals = {
    Common: {
        taxPercent: 5,
        products: [],
        categories: [],
        findProductById: function(id) {
            if (!this.products) {
                return null;
            }

            for(let p of this.products) {
                if (p.id == id) {
                    return p;
                }
            }

            return null;
        },
        calculateTax: function(price) {
            return price * (this.taxPercent / 100);
        }
    },
    Orders: {
    }
}
