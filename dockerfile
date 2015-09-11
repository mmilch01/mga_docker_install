RUN cp -R res/nrgpackages /nrgpackages/
RUN cp -R res/opt		  /opt

RUN apt-get install imagemagick
RUN apt-get install csh
RUN apt-get install gawk
RUN apt-get install bc
#RUN apt-get install libgortran3
#RUN ln -s /usr/lib/x86_64-linux-gnu/libgfortran.so.3 /usr/lib/x86_64-linux-gnu/libgfortran.so.1
RUN cp /host/res/libgfortran.so.1.0.0 /lib/x86_64-linux-gnu/
RUN ln -s /usr/lib/x86_64-linux-gnu/libgfortran.so.1.0.0 /usr/lib/x86_64-linux-gnu/libgfortran.so.1
RUN apt-get install mricron
RUN apt-get install default-jre
RUN apt-get install xvfb
#RUN apt-get install procps