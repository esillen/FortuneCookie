import nltk
import numpy as np
import en


def getUniqueItems(iterable):
    seen = set()
    result = []
    for item in iterable:
        if item not in seen:
            seen.add(item)
            result.append(item)
    return result

def checkIfInSentence(word,sentence):
    for i in range(len(sentence)):
        if(word in sentence[i][0]):
            return i
    return -1

def checkForOtherWordtypes(wordtype,sentence):
    wordsInSentence = []
    for i in range(len(sentence)):
        if(wordtype == sentence[i][1]):
            wordsInSentence.append(sentence[i][0])
    return wordsInSentence

def findPosOfWordInArray(word,array):
    for pos in range(len(array)):
        if(array[pos] == word):
            return pos

def normalizeMatrix(mat):
    rowSums = mat.sum(axis=1)
    #print(mat)
    #print(rowSums)
    [R,C] = mat.shape;
    print(C)
    for colNum in range(C):
        for rowNum in range(R):
            if(rowSums[rowNum] != 0):
                mat[rowNum][colNum] = mat[rowNum][colNum]/rowSums[rowNum]
    return mat

def writeInFile(arrayA,arrayB,mat,fileName):

    a = str(mat)[1:-1]
    a = a.replace("["," ")
    a = a.replace("]"," ")



    file = open(fileName, "w")
    file.write(' '.join(arrayA))
    file.write('\n')
    file.write(' '.join(arrayB))
    file.write('\n')
    file.write(a)
    file.close()



final_list = []
list_part = []
test = []
with open("test.txt") as f:
    for line in f:
		text = nltk.word_tokenize(line)
		text2 = nltk.pos_tag(text)
		#print(text2)
		compare_string = nltk.word_tokenize("make dog Twas made raths outgrabe he asks") #ugly but works for now, will look into it more: VB, NN, NNP, VBD, NNS, VBP
		cp = nltk.pos_tag(compare_string)

		#print en.noun.singular("cows")
		for w in range(len(text2)):
			if text2[w][1] is cp[0][1]:
				list_part.append(text2[w])
			elif text2[w][1] is cp[1][1]:
				list_part.append(text2[w])
			elif text2[w][1] is cp[2][1]:
				text2[w][1] = cp[1][1]
				list_part.append(text2[w])
			elif text2[w][1] is cp[3][1]:
				temp = str(text2[w][0])
				temp  = en.verb.present(temp)
				test = [temp, cp[0][1]]
				list_part.append(tuple(test))
			elif text2[w][1] is cp[5][1]:
				test = [text2[w][0], cp[0][1]]
				#text2[w][1] = cp[0][1]
				list_part.append(tuple(test))
			elif text2[w][1] is cp[4][1]:
				temp = str(text2[w][0])
				temp = en.noun.singular(str((text2[w][0])))
				test = [temp, cp[1][1]]
				#text2[w][1] = tuple(cp[1][1])
				list_part.append(tuple(test))
			elif text2[w][1] is cp[7][1]:
				temp = str(text2[w][0])
				temp  = en.verb.present(temp)
				test = [temp, cp[0][1]]
				list_part.append(tuple(test))

		if len(list_part)!=0:
			final_list.append(list_part)
			list_part = []
			test = []
index = 0





nouns =[]
verbs = []

for sentNum in range(len(final_list)):
    for wordNum in range(len(final_list[sentNum])):
        taggedWord = final_list[sentNum][wordNum]
        if(taggedWord[1] == "NN"):
            nouns.append(taggedWord[0])
        elif(taggedWord[1]=="VB"):
            verbs.append(taggedWord[0])

NN = np.zeros((len(nouns),len(nouns)))
NV = np.zeros((len(nouns),len(verbs)))

nouns = getUniqueItems(nouns)
verbs = getUniqueItems(verbs)

for nounNum in range(len(nouns)):
    for sentenceNum in range(len(final_list)):
        posOfWord = checkIfInSentence(nouns[nounNum],final_list[sentenceNum])
        if(posOfWord != -1):
            verbsInSentence = checkForOtherWordtypes("VB",final_list[sentenceNum])
            if(len(verbsInSentence)!=0):
                for word in verbsInSentence:
                    pos = findPosOfWordInArray(word,verbs)
                    NV[nounNum][pos] = NV[nounNum][pos]+1
            nounsInSentence = checkForOtherWordtypes("NN",final_list[sentenceNum])
            if(len(nounsInSentence)!=0):
                for word in nounsInSentence:
                    pos = findPosOfWordInArray(word,nouns)
                    NN[nounNum][pos] = NN[nounNum][pos]+1


NV = normalizeMatrix(NV)
NN = normalizeMatrix(NN)

#writeInFile(nouns,verbs,NV,"NV.txt")
#writeInFile(nouns,nouns,NN,"NN.txt")