/*
 * Copyright Washington University in St Louis 2006
 * All rights reserved
 *
 * @author Mohana Ramaratnam (Email: mramarat@wustl.edu)

*/

package org.apache.turbine.app.cnda_xnat.modules.actions;

import org.apache.log4j.Logger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.nrg.pipeline.PipelineRepositoryManager;
import org.nrg.pipeline.launchers.*;
import org.nrg.xdat.om.FsFsdata;
import org.nrg.xdat.om.XnatMrsessiondata;
import org.nrg.xdat.turbine.modules.actions.SecureAction;
import org.nrg.xdat.turbine.utils.AdminUtils;
import org.nrg.xdat.turbine.utils.TurbineUtils;
import org.nrg.xft.ItemI;

public class BuildAction extends SecureAction {

    static org.apache.log4j.Logger logger = Logger.getLogger(BuildAction.class);

    public void doPerform(RunData data, Context context){
        if (data.getParameters().getString("refresh") !=null) {
            String refresh = data.getParameters().getString("refresh");
            if (refresh.equalsIgnoreCase("PipelineRepository")) {
                doReload(data,context);
            }
        }
    }

    public void doReload(RunData data, Context context){
        try {
            PipelineRepositoryManager.Reset();
            String msg = "<p><b>The site wide pipeline repository has been refreshed</b></p>";
            data.setMessage(msg);
        }catch(Exception e) {
            logger.debug("Unable to refresh the pipeline repository ", e);
        }
    }

    public void doStdbuild(RunData data, Context context){
        StdBuildLauncher stdBuild = new StdBuildLauncher();
        stdBuild.launch(data, context);
        data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
        data.setScreenTemplate("ClosePage.vm");
    }

    public void doGenericboldpreprocessing(RunData data, Context context){
        GenericBoldPreProcessingLauncher stdBuild = new GenericBoldPreProcessingLauncher();
        boolean rtn = stdBuild.launch(data, context);
        if (rtn)
        data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
        else {
        String msg = data.getMessage();
        data.setMessage("<p><b>The build process was not successfully launched due to " + msg +" Please contact" +AdminUtils.getAdminEmailId() + ".</b></p>");

        }
        data.setScreenTemplate("ClosePage.vm");
    }

    public void doGenericboldpreprocessingpetmr(RunData data, Context context){
        GenericBoldPreProcessing_PetMRLauncher stdBuild = new GenericBoldPreProcessing_PetMRLauncher();
        boolean rtn = stdBuild.launch(data, context);
        if (rtn)
        data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
        else {
        String msg = data.getMessage();
        data.setMessage("<p><b>The build process was not successfully launched due to " + msg +" Please contact" +AdminUtils.getAdminEmailId() + ".</b></p>");

        }
        data.setScreenTemplate("ClosePage.vm");
    }

    public void doPetpreprocessing(RunData data, Context context){
        DianPetProcessing stdBuild = new DianPetProcessing();
        boolean rtn = stdBuild.launch(data, context);
        if (rtn)
        data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
        else {
        String msg = data.getMessage();
        data.setMessage("<p><b>The build process was not successfully launched due to " + msg +" Please contact" +AdminUtils.getAdminEmailId() + ".</b></p>");

        }
        data.setScreenTemplate("ClosePage.vm");
    }


    public void doGenericdtiprocessing(RunData data, Context context){
        GenericDTIProcessingLauncher stdBuild = new GenericDTIProcessingLauncher();
        boolean rtn = stdBuild.launch(data, context);
        if (rtn) {
            data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
        }else {
            data.setMessage("<p><b>The build process was not successfully launched. Please contact" +AdminUtils.getAdminEmailId() + ".</b></p>");
        }
        data.setScreenTemplate("ClosePage.vm");
    }

    public void doBirnphantomqa(RunData data, Context context){
        BirnPhantomQA stdBuild = new BirnPhantomQA();
        boolean rtn = stdBuild.launch(data, context);
        if (rtn) {
            data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
        }else {
            data.setMessage("<p><b>The build process was not successfully launched. Please contact" +AdminUtils.getAdminEmailId() + ".</b></p>");
        }
        data.setScreenTemplate("ClosePage.vm");
    }

    public void doBoldseedanalysis(RunData data, Context context){
        BoldSeedBasedAnalysis seedAnalysis = new BoldSeedBasedAnalysis();
        boolean rtn = seedAnalysis.launch(data, context);
        if (rtn) {
            data.setMessage("<p><b>The seed based analysis was successfully launched. Status email will be sent upon its completion.</b></p>");
        }else {
            data.setMessage("<p><b>Error. Please contact the site administrator</b></p>");
        }
        data.setScreenTemplate("ClosePage.vm");
    }

    public void doFreesurfer(RunData data, Context context){
        try {
            ItemI data_item = TurbineUtils.GetItemBySearch(data);
            XnatMrsessiondata mr = new XnatMrsessiondata(data_item);
            FreesurferLauncher freesurfer = new FreesurferLauncher(data, mr);
            boolean success = freesurfer.launch(data, context, mr);
            if (success ) {
                data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
            }else {
                data.setMessage("<p><b>The build process couldn't be launched. </b></p>");
            }
            data.setScreenTemplate("ClosePage.vm");
        }catch(Exception e) {
            logger.debug(e);
        }
    }

    public void doFreesurferrelaunch(RunData data, Context context){
        try {
            ItemI data_item = TurbineUtils.GetItemBySearch(data);
            FsFsdata fs = new FsFsdata(data_item);
            FreesurferRelauncher freesurfer = new FreesurferRelauncher( fs);
            boolean success = freesurfer.launch(data, context);
            if (success ) {
                data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
            }else {
                data.setMessage("<p><b>The build process couldn't be launched. </b></p>");
            }
            data.setScreenTemplate("ClosePage.vm");
        }catch(Exception e) {
            logger.debug(e);
        }
    }

    public void doFreesurfer_relaunch_after_manualupload(RunData data, Context context){
        try {
            ItemI data_item = TurbineUtils.GetItemBySearch(data);
            FsFsdata fs = new FsFsdata(data_item);

            FreesurferRelauncherAfterManualUpload freesurfer = new FreesurferRelauncherAfterManualUpload( fs);
            boolean success = freesurfer.launch(data, context);
            if (success ) {
                data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
            }else {
                data.setMessage("<p><b>The build process couldn't be launched. </b></p>");
            }
            data.setScreenTemplate("ClosePage.vm");
        }catch(Exception e) {
            logger.debug(e);
        }
    }

    public void doHofanalysis(RunData data, Context context)
    {
        try {
            HOFLauncher hof = new HOFLauncher();
            boolean success = hof.launch(data, context);
            if (success ) {
                data.setMessage("<p><b>The HOF build process was successfully launched. Status email will be sent upon its completion.</b></p>");
            }else {
                data.setMessage("<p><b>The HOF build process could not be launched. </b></p>");
            }
            data.setScreenTemplate("ClosePage.vm");
        }catch(Exception e) {
            logger.debug(e);
        }
    }
    
    public void doFacemasking(RunData data, Context context){
        try {
            ItemI data_item = TurbineUtils.GetItemBySearch(data);
            XnatMrsessiondata mr = new XnatMrsessiondata(data_item);
            FaceMaskingLauncher facemask = new FaceMaskingLauncher(data, mr);
            boolean success = facemask.launch(data, context, mr);
            if (success ) {
                data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
            }else {
                data.setMessage("<p><b>The build process could not be launched. </b></p>");
            }
            data.setScreenTemplate("ClosePage.vm");
        }catch(Exception e) {
            logger.debug(e);
        }
    }

    public void doDicomtonifti(RunData data, Context context){
        try {
            DicomToNiftiLauncher dcm = new DicomToNiftiLauncher();
            boolean success = dcm.launch(data, context);
            if (success ) {
                data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
            }else {
                data.setMessage("<p><b>The build process could not be launched. </b></p>");
            }
            data.setScreenTemplate("ClosePage.vm");
        }catch(Exception e) {
            logger.debug(e);
        }
    }

    public void doHcpdicomtonifti(RunData data, Context context){
        try {
            HCPDicomToNiftiLauncher dcm = new HCPDicomToNiftiLauncher();
            boolean success = dcm.launch(data, context);
            if (success ) {
                data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
            }else {
                data.setMessage("<p><b>The build process could not be launched. </b></p>");
            }
            data.setScreenTemplate("ClosePage.vm");
        }catch(Exception e) {
            logger.debug(e);
        }
    }

    public void doAdembuild(RunData data, Context context){
        AdemPipelineLauncher stdBuild = new AdemPipelineLauncher();
        stdBuild.launch(data, context);
        data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
        data.setScreenTemplate("ClosePage.vm");
    }

    public void doDtiqc(RunData data, Context context){
        DtiQcLauncher dtiQc = new DtiQcLauncher();
        dtiQc.launch(data, context);

        data.setMessage("<p><b>The build process was successfully launched. Status email will be sent upon its completion.</b></p>");
        data.setScreenTemplate("ClosePage.vm");
    }
}