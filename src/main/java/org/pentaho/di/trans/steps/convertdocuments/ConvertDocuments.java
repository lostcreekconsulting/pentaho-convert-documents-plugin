package org.pentaho.di.trans.steps.convertdocuments;

import java.io.IOException;
import java.io.File;

import org.artofsolving.jodconverter.office.OfficeManager;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.OfficeDocumentConverter;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

public class ConvertDocuments extends BaseStep implements StepInterface {
    private static Class<?> PKG = ConvertDocumentsMeta.class;
    private OfficeManager manager;
    private OfficeDocumentConverter converter;
    private Process process;

    public ConvertDocuments(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        ConvertDocumentsMeta meta = (ConvertDocumentsMeta) smi;
        ConvertDocumentsData data = (ConvertDocumentsData) sdi;

        manager = new DefaultOfficeManagerConfiguration()
			.setOfficeHome(meta.getProgramPath())//"c:\\Program Files (x86)\\LibreOffice 4")
			.buildOfficeManager();
		manager.start();
        converter = new OfficeDocumentConverter(manager);

        return super.init(meta, data);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
        ConvertDocumentsMeta meta = (ConvertDocumentsMeta) smi;
        ConvertDocumentsData data = (ConvertDocumentsData) sdi;

        Object[] r = getRow();

        if (r == null) {
            setOutputDone();
            return false;
        }

        if (first) {
            first = false;
            data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this);
        }

        File source = new File((String) r[data.outputRowMeta.indexOfValue(meta.getDynamicSourceFileNameField())]);
        File target = new File((String) r[data.outputRowMeta.indexOfValue(meta.getDynamicTargetFileNameField())]);
        File targetFolder = target.getParentFile();

        if (!target.exists() || meta.isOverwriteTarget()) {
            if (!targetFolder.exists() && meta.isCreateParentFolder()) {
                targetFolder.mkdir();
            }
    
            if (targetFolder.exists()) {
                converter.convert(source, target);
            }
        }

        if (meta.isDeleteSource()) {
            source.delete();
        }
        
        putRow(data.outputRowMeta, r);

        return true;
    }

    public void dipose(StepMetaInterface smi, StepDataInterface sdi) {
        ConvertDocumentsMeta meta = (ConvertDocumentsMeta) smi;
        ConvertDocumentsData data = (ConvertDocumentsData) sdi;

        manager.stop();

        super.dispose(meta, data);
    }
}
