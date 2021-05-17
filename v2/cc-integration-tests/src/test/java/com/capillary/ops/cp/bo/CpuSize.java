package com.capillary.ops.cp.bo;

import java.util.Objects;

public class CpuSize {

    Double amount;

    CpuUnits units;

    public CpuSize(Double amount, CpuUnits cpuUnits) {
        this.amount = amount;
        this.units = cpuUnits;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public CpuUnits getUnits() {
        return units;
    }

    public void setUnits(CpuUnits units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "CpuSize{" +
                "amount=" + amount +
                ", units=" + units +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CpuSize)) return false;
        CpuSize cpuSize = (CpuSize) o;
        if (getUnits() == cpuSize.getUnits()) {
            return getAmount().equals(cpuSize.getAmount());
        } else {
            CpuUnits units = cpuSize.getUnits();
            boolean result;
            switch (units) {
                case Cores:
                    result = getAmount().equals(cpuSize.getAmount()*1000);
                    break;
                case MilliCores:
                    result = cpuSize.getAmount().equals(getAmount()*1000);
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
