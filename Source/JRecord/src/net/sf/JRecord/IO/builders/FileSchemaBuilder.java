package net.sf.JRecord.IO.builders;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.sf.JRecord.External.CopybookLoaderFactory;
import net.sf.JRecord.External.ExternalRecord;
import net.sf.JRecord.External.ICopybookLoaderStream;
import net.sf.JRecord.External.RecordEditorXmlWriter;
import net.sf.JRecord.Log.TextLog;
import net.sf.JRecord.Numeric.ICopybookDialects;
import net.sf.JRecord.def.IO.builders.ICobolIOBuilder;
import net.sf.JRecord.def.IO.builders.ICobolCopybookIOProvider;
import net.sf.JRecord.def.IO.builders.ICobolMultiCopybookIOBuilder;
import net.sf.JRecord.def.IO.builders.IIOCopybookProvider;

public class FileSchemaBuilder implements ICobolCopybookIOProvider, IIOCopybookProvider {
	private static final CopybookLoaderFactory lf = CopybookLoaderFactory.getInstance();
	
	private final int schemaType;

	public FileSchemaBuilder(int schemaType) {
		super();
		this.schemaType = schemaType;
	}
	
/* (non-Javadoc)
 * @see net.sf.JRecord.IO.builders.ICobolIOCopybookProvider#newIOBuilder(java.lang.String)
 */
	@Override
	public CblIOBuilderSchemaFilename newIOBuilder(String copybookFileame) {
    	try {
			return new CblIOBuilderSchemaFilename(copybookFileame, lf.getLoader(schemaType), ICopybookDialects.FMT_MAINFRAME);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
    }

	
	/* (non-Javadoc)
	 * @see net.sf.JRecord.IO.builders.ICobolIOCopybookProvider#newIOBuilder(java.io.InputStream, java.lang.String)
	 */
	@Override
	public ICobolIOBuilder newIOBuilder(InputStream cobolCopybookStream, String copybookName) {
    	try {
			return new CblIOBuilderSchemaStream(
					cobolCopybookStream, copybookName, 
					(ICopybookLoaderStream) lf.getLoader(schemaType), 
					ICopybookDialects.FMT_MAINFRAME);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

	/**
	 * @see net.sf.JRecord.IO.builders.ICobolIOCopybookProvider#newMultiCopybookIOBuilder(java.lang.String)
	 */
	@Override 
	public ICobolMultiCopybookIOBuilder newMultiCopybookIOBuilder(String copybookname) {
		try {
			return new CblIOBuilderMultiSchema(copybookname, (ICopybookLoaderStream) lf.getLoader(schemaType));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.JRecord.def.IO.builders.IIOCopybookProvider#export(java.lang.String, net.sf.JRecord.External.ExternalRecord)
	 */
	@Override
	public void export(String fileName, ExternalRecord schema) throws Exception {
		export(new BufferedOutputStream(new FileOutputStream(fileName)), schema);
	}

	/* (non-Javadoc)
	 * @see net.sf.JRecord.def.IO.builders.IIOCopybookProvider#export(java.io.OutputStream, net.sf.JRecord.External.ExternalRecord)
	 */
	@Override
	public void export(OutputStream outStream, ExternalRecord schema) throws Exception {
		RecordEditorXmlWriter writer = new RecordEditorXmlWriter();
		
		writer.writeCopyBook(outStream, schema, new TextLog());
	}
	
	
} 