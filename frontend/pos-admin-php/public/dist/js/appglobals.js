let AppGlobals = {
    Common: {
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
        }
    },
    Orders: {
    }
}
