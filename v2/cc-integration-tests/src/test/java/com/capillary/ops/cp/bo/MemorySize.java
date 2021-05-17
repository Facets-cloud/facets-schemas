package com.capillary.ops.cp.bo;

import java.util.Objects;

public class MemorySize {

    Double amount;

    MemoryUnits units;

    public MemorySize(Double amount, MemoryUnits units){
        this.amount = amount;
        this.units = units;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public MemoryUnits getUnits() {
        return units;
    }

    public void setUnits(MemoryUnits units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "MemorySize{" +
                "amount=" + amount +
                ", units=" + units +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemorySize)) return false;
        MemorySize memorySize = (MemorySize) o;
        if (getUnits() == memorySize.getUnits()) {
            return getAmount().equals(memorySize.getAmount());
        }
        else {
            MemoryUnits units = memorySize.getUnits();
            boolean result;
            switch (units) {
                case MegaBytes:
                    result = memorySize.getAmount().equals(getAmount()*1024);
                    break;
                case GigaBytes:
                    result = getAmount().equals(memorySize.getAmount()*1024);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + units);
            }
            return result;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount(), getUnits());
    }
}
