infile = open("korpus2.txt")
outfile = open("korpus2_edited.txt","w")
for line in infile.readlines():
    inBracket = False
    for letter in line:
        if letter == "<":
            inBracket = True
        elif letter ==">":
            inBracket = False
        elif inBracket == False:
            
            outfile.write(letter)




infile.close()

outfile.close()


