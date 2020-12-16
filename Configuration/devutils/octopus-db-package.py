#!/usr/bin/python
# --------------------------------------------------------------------------------------------------
#  This python script packages  sql files listed in a manifest into a valid octopus  CHOX db package
#
#  Parameters
#  -h : help
#  -c : clear destination folder
#  -i <manifest_file> : input manifest file listing all sql scripts to be included
#  -o <db_package_folder> : full path to the root folder for new db package. The folder should not exist
#  -t <template> : template db folder, eg. Octopus-DB in the chox project source directory
#  Paths to sql files in the manifest are relative to the location of the manifest file
#
#  Unit tests :
#  Default working case : python Configuration/devutils/octopus-db-package.py -c
#  Error - destination exists : python Configuration/devutils/octopus-db-package.py -o  Configuration/devutils/../../Octopus-DB
#  Error - Invalid manifest : python Configuration/devutils/octopus-db-package.py -i blah
#  Error - Invalid manifest content : echo "blah.sql" > dummy.txt; python Configuration/devutils/octopus-db-package.py -i dummy.txt
# --------------------------------------------------------------------------------------------------
import sys, getopt, os, shutil

def usage(me):
   print("Usage : python3 {} -h | [-c] [-i <manifest_file>] [-o <final_db_package_folder>] [-t <octopus_db_template_folder>]".format(me))
   print("Example : \npython Configuration/devutils/octopus-db-package.py -i Configuration/DBScript/manifest_GTA-4.14-Changes.txt -o CHOX-DB-GTA-4.14-Changes  -t Octopus-DB")

def mk_package_folders (src, dest):
   """
   Create a new db package folder structure as a clone of the template provided 
   """
   try:
      shutil.copytree(src, dest)
   except shutil.Error as e:
      # Directories are the same
      print('Error - Directory not copied. Exception: %s' % e)
      sys.exit(2)
   except OSError as e:
      # Any error saying that the directory doesn't exist
      print('Error - Directory not copied. Exception: %s' % e)
      sys.exit(2)
   
def populate_package(manifest, packageroot):
   """
   Read manifest and populate a db package folder tree with the listed sql files
   Creates the 00.runAll.sql script
   """
   location = os.path.join(packageroot, 'ROLLFORWARD/CHOX')
   pathtodata = os.path.dirname(manifest)

   try:
      os.remove(os.path.join(location, ".gitkeep"))
   except OSError as e:
      print ('Warning - .gitkeep not found - possibly the template folder is not checked in git?')
   fh = open (os.path.join(location, "00.runAll.sql"), "w")


   count = 0;
   try:
      with  open (manifest, "r") as f:
         
         for line in f:
            count+=1
            scriptname = line.strip()
            if scriptname.startswith('#'):
               continue
            if len(scriptname) == 0 :
               continue

            scriptname = os.path.join(pathtodata, scriptname)

            scriptnameprefixed =  "{:02d}.{}".format(count, os.path.basename(scriptname))
            #print ('scriptname'+scriptname+' '+'scriptnameprefixed='+scriptnameprefixed)
            try:
               shutil.copyfile(scriptname, os.path.join(location, scriptnameprefixed))
            except shutil.Error as e:
               print('Error - Could not copy sql script file. Exception: %s' % e)
               sys.exit(2)
            fh.write("\i {}\n".format(scriptnameprefixed)) 
   except IOError as e:
      print ("Error : Could not process file {} - {} ".format(manifest, e) )
      sys.exit(2)

   fh.close()


def main(me, argv):
   """
   Entry point - do the job after parsing command line parameters
   Exactly 2*3 parameters are expected 
   """
   inputfile = ''
   outputfolder = ''
   templatefolder = ''
   cleardest = False

   try:
      opts, args = getopt.getopt(argv,"hci:o:t:",["ifile=","ofile=","tfolder="])
   except getopt.GetoptError:
      print("Error: incorrect parameters")
      usage(me)
      sys.exit(2)
   for opt, arg in opts:
      if opt == '-h':
         usage(me)
         sys.exit()
      elif opt in ("-c", "--clear"):
         cleardest = True
      elif opt in ("-i", "--ifile"):
         inputfile = arg
      elif opt in ("-o", "--ofile"):
         outputfolder = arg
      elif opt in ("-t", "--tfolder"):
         templatefolder = arg

   # Use default values where missing
   scriptpath = os.path.dirname(me)
   if len(inputfile) == 0 :
      inputfile = os.path.join(scriptpath, "../DBScript/manifest_latest.txt")

   if len(outputfolder) == 0 :
      outputfolder = os.path.join(scriptpath, "../../Octopus-DB-Latest")

   if len(templatefolder) == 0 :
      templatefolder= os.path.join(scriptpath, "../../Octopus-DB")

   if (cleardest):
      print ('Warning : Deleting existing folder {} - ensure the changes are not checked in git!'.format(outputfolder))
      try:
         shutil.rmtree(outputfolder)
      except Exception as e:
         print('Error - Could not delete folder. Exception: %s' % e)
         sys.exit(2)


   mk_package_folders(templatefolder, outputfolder)
   populate_package(inputfile, outputfolder)
   print ("created db package folder {} with scripts listed in manifest {}".format(outputfolder, inputfile))

if __name__ == "__main__":
   main(sys.argv[0], sys.argv[1:])



 




