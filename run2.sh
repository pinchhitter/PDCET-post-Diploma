javac cdac/in/nbe/Allocator.java 
#java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv -lr ../pdcet-data/pdcet-lastround-applicant.csv -r 2 -rs > ../pdcet-data/vacancy-matrix-pdcet.csv
java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv -lr ../pdcet-data/pdcet-lastround-applicant.csv -r 2 -tv > ../pdcet-data/allocation-pdcet.csv
java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv -lr ../pdcet-data/pdcet-lastround-applicant.csv -r 2 -cs > ../pdcet-data/course-status-pdcet.csv
java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv -lr ../pdcet-data/pdcet-lastround-applicant.csv -r 2 -dnb > ../pdcet-data/nbe-view-pdcet.csv

