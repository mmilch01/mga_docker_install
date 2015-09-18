/*
 * HOF pipeline screen v. 01/22/2014
*/

package org.apache.turbine.app.cnda_xnat.modules.screens;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.nrg.pipeline.launchers.GenericBoldPreProcessingLauncher;
import org.nrg.pipeline.launchers.StdBuildLauncher;
import org.nrg.pipeline.utils.PipelineUtils;
import org.nrg.xdat.model.XnatImagescandataI;
import org.nrg.xdat.om.ArcPipelineparameterdata;
import org.nrg.xdat.om.WrkWorkflowdata;
import org.nrg.xdat.om.XnatMrsessiondata;
import org.nrg.xdat.turbine.utils.TurbineUtils;
import org.nrg.xnat.turbine.modules.screens.DefaultPipelineScreen;

public class PipelineScreen_HOF extends DefaultPipelineScreen 
{
	String m_script_path;
	String[] m_scanlib_ids;
    static Logger logger = Logger.getLogger(PipelineScreen_HOF.class);	
	public void finalProcessing(RunData data, Context context)
	{
		XnatMrsessiondata mr = (XnatMrsessiondata) om;
        context.put("mr", mr);
		try
		{
			m_script_path = getProjectPipelineSetting("HOF_SCRIPT_PATH").getCsvvalues();

		}catch(Exception e)
		{
			logger.error("Looks like HOF_SCRIPT_PATH was not set in the pipeline project settings");			
			e.printStackTrace();
		}
		//populate scanlib id's
		String prefix=m_script_path+"/";
		prefix.replaceAll("//", "/");
		String command=prefix+"slist li";
		m_scanlib_ids=executeCommand(command).replaceAll("\\n", "").split(" ");
		String desc,sid;
		String[] hofparams;
		LinkedList<XnatImagescandataI> scans;
		
		LinkedHashMap<String,String> buildableScans=new LinkedHashMap<String,String>();
        for(XnatImagescandataI scan :  mr.getScans_scan())
        {
        	desc=scan.getSeriesDescription();
        	if(desc==null) continue;
        	desc=desc.replaceAll("\\\\n", "").replaceAll("\\s","");
        	hofparams=executeCommand(prefix+"slist qd "+desc).split(" ");
        	if(hofparams.length>=4)
        	{
        		buildableScans.put(scan.getId(), hofparams[1]);
        	}
        }
        context.put("buildableScans", buildableScans);
        context.put("projectSettings", projectParameters);
        context.put("HOF_IDs", m_scanlib_ids);
	}
    public void preProcessing(RunData data, Context context) {
        super.preProcessing(data, context);	
    }
	private String executeCommand(String command)
	{
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
	                        new BufferedReader(new InputStreamReader(p.getInputStream()));
	
	        String line = "";			
			while ((line = reader.readLine())!= null) 
			{
				output.append(line); //+ "\n");
			}	
		}
		catch (Exception e) 
		{
			logger.error("Error executing system command: "+command);
			e.printStackTrace();
		}	
		return output.toString();	
	}
}
