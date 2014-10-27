package org.pentaho.di.trans.steps.convertdocuments;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class ConvertDocumentsData extends BaseStepData implements StepDataInterface {
    public RowMetaInterface outputRowMeta;

    public ConvertDocumentsData() {
        super();
    }
}
