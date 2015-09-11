#!/bin/bash

#check prerequisities.
command -v docker &> /dev/null 2>&1 || { echo "HOF requires docker but it's not installed. Aborting." >&2; echo 1; }
docker pull xnat/hof:release
#docker load -i=hof_image.tar
#cp -p rhof $HOF_HOME
#export PATH=$PATH:$HOF_HOME

#create HOF user configuration.
mkdir -p ~/.hof_db
cp DB/hofid_udescr ~/.hof_db/

RT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#set up pipeline xml.
n=0
while [ ! -f "$hof_path/HOF.xml" ]; do
	if (( n > 2 )); then
		echo "Cannot locate HOF.xml, exiting"
		exit -1
	fi
	if (( n > 0 )); then
		echo "File $hof_path/HOF.xml does not exist, please try again."
	fi
	echo "Please enter the location of HOF pipeline XML, e.g. /data/XNAT/pipeline/catalog/HOF, followed by [ENTER]:" 
	read hof_path
	(( n++ ))
done

pushd `dirname $0` > /dev/null
RC=`pwd`
popd > /dev/null
#set up hof dir in pipeline xml.
sed -i "s:%HOF_HOME%:$RC:g" $hof_path/HOF.xml
sed -i "s:%HOF_HOME%:$RC:g" $hof_path/resource/rhof.xml
echo "Done"
exit 0
