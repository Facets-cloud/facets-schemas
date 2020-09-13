package com.capillary.ops.cp.bo;

public class TerraformStep {
    public enum RunType {
        plan(false),
        apply(true);

        private boolean autoApprove;

        private RunType(boolean autoApprove) {
            this.autoApprove = autoApprove;
        }

        public boolean isAutoApprove() {
            return autoApprove;
        }
    }

    public TerraformStep() {}

    public TerraformStep(RunType type, String target, Integer parallelism) {
        this.type = type;
        this.target = target;
        this.parallelism = parallelism;
    }

    private static final Integer DEFAULT_PARALLELISM = 10;

    private RunType type;

    private String target;

    private Integer parallelism = DEFAULT_PARALLELISM;

    public RunType getType() {
        return type;
    }

    public void setType(RunType type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getParallelism() {
        return parallelism;
    }

    public void setParallelism(Integer parallelism) {
        this.parallelism = parallelism;
    }

    public String toTFCommand() {
        String autoApprove = type.isAutoApprove() ? "-auto-approve" : "";
        return String.format("terraform %s -target %s %s -no-color -parallelism=%s", type.name(), target, autoApprove,
                parallelism);
    }
}
