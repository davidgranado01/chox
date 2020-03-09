import sys
import os
import csv

arguments_len = len(sys.argv) - 1
data_folder = '.'
if arguments_len is 1:
    data_folder = sys.argv[1]

if not os.path.exists(data_folder):
    print("Please specify the 'backups' folder and make sure backups exists.")
    exit(1)

def process_file(file_path):
    with open(file_path) as datas:
        data_reader = csv.reader(datas, delimiter='\t')
        for data in data_reader:
            if "gmail_scheduler_job" in file_path:
                print("UPDATE gmail_scheduler_job SET login_password = '%s' WHERE id = %s;" % (
                    data[2], data[0]))
            elif "scheduler_job" in file_path:
                print("UPDATE scheduler_job SET login_password = '%s' WHERE id = %s;" % (
                    data[2], data[0]))

processed = False
for root, directories, files in os.walk(data_folder):
    for file in files:
        if "backup" in file:
            processed = True
            print("\n")
            process_file(os.path.join(root, file))


if not processed:
    print("Cannot find backup files.")
    exit(2)
