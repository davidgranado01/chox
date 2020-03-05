#!/usr/bin/python
# --------------------------------------------------------------------------------------------------
#  This python script packages  sql files listed in a manifest into a valid octopus  CHOX db package
# --------------------------------------------------------------------------------------------------
import sys, getopt, os, shutil

def usage(me):
   print("Usage : python3 {} -i <inputfile> -o <outputfile> -t <template folder>".format(me))

def mk_package_folders (src, dest):
    try:
        shutil.copytree(src, dest)
    # Directories are the same
    except shutil.Error as e:
        print('Directory not copied. Error: %s' % e)
    # Any error saying that the directory doesn't exist
    except OSError as e:
        print('Directory not copied. Error: %s' % e)
   
def read_input(manifest, packageroot):
   print ('Manifest file is :' +  manifest)
   location = os.path.join(packageroot, 'ROLLFORWARD/CHOX')
   print ('location='+location)

   fh = open (os.path.join(location, "00.runAll.sql"), "w")

   count = 0;
   try:
      with  open (manifest, "r") as f:
         
         for line in f:
            count+=1
            scriptname = line.strip()
            scriptnameprefixed =  "{:02d}.{}".format(count, os.path.basename(scriptname))
            print ('scriptname'+scriptname+' '+'scriptnameprefixed='+scriptnameprefixed)
            shutil.copyfile(scriptname, os.path.join(location, scriptnameprefixed))
            fh.write("\i {}\n".format(scriptnameprefixed)) 
   except IOError as e:
      print ("Could not read file {} - {} ".format(manifest, e) )

   fh.close()

def main(me, argv):
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
   print (templatefolder)
   read_input(inputfile, outputfile)
   print ('Output folder is '+ outputfile)

if __name__ == "__main__":
   main(sys.argv[0], sys.argv[1:])



 




