package com.manager.app_ecommerce.Model.EventBus;

import com.manager.app_ecommerce.Model.Invoice;

public class InviceEvent {
    Invoice invoice;

    public InviceEvent(Invoice invoice) {
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
