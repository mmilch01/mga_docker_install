/*
 * org.nrg.pipeline.launchers.HOFLauncher
 * XNAT http://www.xnat.org
 * Copyright (c) 2014, Washington University School of Medicine
 * All Rights Reserved
 *
 * Released under the Simplified BSD.
 *
*/

package org.nrg.pipeline.launchers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.nrg.pipeline.XnatPipelineLauncher;
import org.nrg.pipeline.utils.PipelineFileUtils;
import org.nrg.pipeline.xmlbeans.ParameterData;
import org.nrg.pipeline.xmlbeans.ParametersDocument.Parameters;
import org.nrg.xdat.om.XnatMrsessiondata;
import org.nrg.xdat.turbine.utils.TurbineUtils;
import org.nrg.xft.ItemI;

public class HOFLauncher extends PipelineLauncher{
    static org.apache.log4j.Logger logger = Logger.getLogger(HOFLauncher.class);

    public boolean launch(RunData data, Context context) 
    {
        try {
            ItemI data_item = TurbineUtils.GetItemBySearch(data);            
            XnatMrsessiondata mr = new XnatMrsessiondata(data_item);
            XnatPipelineLauncher xnatPipelineLauncher = XnatPipelineLauncher.GetLauncher(data, context, mr);
            String cmdPrefix = ((String)org.nrg.xdat.turbine.utils.TurbineUtils.GetPassedParameter("cmdprefix",data));            

            String pipelineName = ((String)org.nrg.xdat.turbine.utils.TurbineUtils.GetPassedParameter("pipelinename",data));

            xnatPipelineLauncher.setPipelineName(pipelineName);
            xnatPipelineLauncher.setSupressNotification(true);

            String buildDir = PipelineFileUtils.getBuildDir(mr.getProject(), true);
            buildDir +=  "HOF";

            xnatPipelineLauncher.setBuildDir(buildDir);
            xnatPipelineLauncher.setNeedsBuildDir(false);

            Parameters parameters = Parameters.Factory.newInstance();
            
            ParameterData param = parameters.addNewParameter();
            param.setName("sessionId");
            param.addNewValues().setUnique(mr.getLabel());

            param = parameters.addNewParameter();
            param.setName("xnat_id");
            param.addNewValues().setUnique(mr.getId());

            param = parameters.addNewParameter();
            param.setName("project");
            param.addNewValues().setUnique(mr.getProject());
            
            param = parameters.addNewParameter();
            param.setName("subject");
            param.addNewValues().setUnique(mr.getSubjectId());

            param = parameters.addNewParameter();
            param.setName("scans");
            param.addNewValues().setUnique(((String)org.nrg.xdat.turbine.utils.TurbineUtils.GetPassedParameter("scans",data)));

            param = parameters.addNewParameter();
            param.setName("HOF_IDs");
            param.addNewValues().setUnique(((String)org.nrg.xdat.turbine.utils.TurbineUtils.GetPassedParameter("HOF_IDs",data)));

            param = parameters.addNewParameter();
            param.setName("Siemens_scanner");
            param.addNewValues().setUnique(((String)org.nrg.xdat.turbine.utils.TurbineUtils.GetPassedParameter("Siemens_scanner",data)));

            param = parameters.addNewParameter();
            param.setName("ManualParams");
            param.addNewValues().setUnique(((String)org.nrg.xdat.turbine.utils.TurbineUtils.GetPassedParameter("ManualParams",data)));
            
            String emailsStr = TurbineUtils.getUser(data).getEmail() + "," + data.getParameters().get("emailField");
            String[] emails = emailsStr.trim().split(",");
            for (int i = 0 ; i < emails.length; i++) {
                if (emails[i]!=null && !emails[i].equals(""))  xnatPipelineLauncher.notify(emails[i]);
            }

            String paramFileName = getName(pipelineName);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String s = formatter.format(date);

            paramFileName += "_params_" + s + ".xml";

            String paramFilePath = saveParameters(buildDir + File.separator + mr.getLabel(),paramFileName,parameters);

            xnatPipelineLauncher.setParameterFile(paramFilePath);

            boolean rtn = xnatPipelineLauncher.launch(cmdPrefix);
            return rtn;
        }catch(Exception e) {
            logger.error(e.getCause() + " " + e.getLocalizedMessage());
            return false;
        }
    }

}
