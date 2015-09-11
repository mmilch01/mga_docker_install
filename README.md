Summary
Installation and documentation package for MGA. 
MGA is an MRI preprocessing pipeline built with HOF (Heterogeneous Optimization Framework) methodology. MGA prepares neuro-oncology clinical imaging studies for scientific analysis such as multispectral ROI analysis, in both longitudinal and cross-sectional studies. MGA works on DICOM images from a single MRI study. Includes perfusion (DSC sequence based) analysis and DTI analysis, and spatially co-registers all study images to an atlas template and to a template image within the study. MGA is designed to reasonably minimize user configuration steps and to accept a broad range of MRI submodalities, including support for clinical image quality. The distribution is available as a stand-alone virtual appliance using the docker-io virtualization platform.

Version
0.1

Installation:

1) install docker-io: https://docs.docker.com/installation

2) install MGA runtime package from https://bitbucket.org/mmilch01/mga_docker_install

3) run hof_install.sh

4) add mga_docker_install location to your path

5) done!

Documentation: 

see docs/ directory.

Running:

1) Prepare the data. Copy all DICOM files for the study to <study dir>/DICOM

2) cd <study_dir>

3) Run master script that attempts to automatically detect and process all available DICOM series (see docs for configuration options):

rhof condr_preproc .