#!/usr/bin/python
# --------------------------------------------------------------------------------------------------
#  This python script packages  sql files listed in a manifest into a valid octopus  CHOX db package
#
#  Parameters
#  -h : help
#  -i <manifest_file> : input manifest file listing all sql scripts to be included
#  -o <db_package_folder> : full path to the root folder for new db package. The folder should not exist
#  -t <template> : template db folder, eg. Octopus-DB in the chox project source directory
#  Paths to sql files in the manifest are relative to the location of the manifest file
#
# --------------------------------------------------------------------------------------------------
import sys, getopt, os, shutil

def usage(me):
   print("Usage : python3 {} -i <manifest_file> -o <final_db_package_folder> -t <octopus_db_template_folder>".format(me))
   print("Example : \npython Configuration/devutils/octopus-db-package.py -i Configuration/DBScript/manifest_GTA-4.14-Changes.txt -o CHOX-DB-GTA-4.14-Changes  -t Octopus-DB")

def mk_package_folders (src, dest):
   """
   Create a new db package folder structure as a clone of the template provided 
   """
   try:
      shutil.copytree(src, dest)
   except shutil.Error as e:
      # Directories are the same
      print('Directory not copied. Error: %s' % e)
   except OSError as e:
      # Any error saying that the directory doesn't exist
      print('Directory not copied. Error: %s' % e)
   
def populate_package(manifest, packageroot):
   """
   Read manifest and populate a db package folder tree with the listed sql files
   Creates the 00.runAll.sql script
   """
   location = os.path.join(packageroot, 'ROLLFORWARD/CHOX')
   pathtodata = os.path.dirname(manifest)

   os.remove(os.path.join(location, ".gitkeep"))
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
            shutil.copyfile(scriptname, os.path.join(location, scriptnameprefixed))
            fh.write("\i {}\n".format(scriptnameprefixed)) 
   except IOError as e:
      print ("Could not read file {} - {} ".format(manifest, e) )

   fh.close()


def main(me, argv):
   """
   Entry point - do the job after parsing command line parameters
   Exactly 2*3 parameters are expected 
   """
   inputfile = ''
   outputfile = ''
   if len(argv) != 6 :
      usage(me)
      sys.exit()

   try:
      opts, args = getopt.getopt(argv,"hi:o:t:",["ifile=","ofile=","tfolder="])
   except getopt.GetoptError:
      usage(me)
      sys.exit(2)
   for opt, arg in opts:
      if opt == '-h':
         usage(me)
         sys.exit()
      elif opt in ("-i", "--ifile"):
         inputfile = arg
      elif opt in ("-o", "--ofile"):
         outputfile = arg
      elif opt in ("-t", "--tfolder"):
         templatefolder = arg

   mk_package_folders(templatefolder, outputfile)
   populate_package(inputfile, outputfile)
   print ("created db package folder {} with scripts listed in manifest {}".format(outputfile, inputfile))

if __name__ == "__main__":
   main(sys.argv[0], sys.argv[1:])



 




