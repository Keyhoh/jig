package jig.domain.model.report;

import jig.domain.model.angle.method.MethodConcern;
import jig.domain.model.angle.method.MethodDetail;
import jig.domain.model.characteristic.Characteristic;
import jig.domain.model.report.template.ReportRow;

import java.util.Arrays;

public enum MethodPerspective {
    SERVICE(new MethodConcern[]{
            MethodConcern.クラス名,
            MethodConcern.クラス和名,
            MethodConcern.メソッド,
            MethodConcern.メソッド戻り値の型,
            MethodConcern.イベントハンドラ,
            MethodConcern.使用しているフィールドの型,
            MethodConcern.使用しているリポジトリのメソッド,
    }),
    REPOSITORY(new MethodConcern[]{
            MethodConcern.クラス名,
            MethodConcern.クラス和名,
            MethodConcern.メソッド,
            MethodConcern.メソッド戻り値の型,
            MethodConcern.DB_C,
            MethodConcern.DB_R,
            MethodConcern.DB_U,
            MethodConcern.DB_D
    });

    private final MethodConcern[] concerns;

    MethodPerspective(MethodConcern[] concerns) {
        this.concerns = concerns;
    }

    public ReportRow headerLabel() {
        return Arrays.stream(concerns)
                .map(Enum::name)
                .collect(ReportRow.collector());
    }

    public ReportRow row(MethodDetail methodDetail) {
        return Arrays.stream(concerns)
                .map(concern -> concern.apply(methodDetail))
                .collect(ReportRow.collector());
    }

    public Characteristic characteristic() {
        return Characteristic.valueOf(name());
    }
}