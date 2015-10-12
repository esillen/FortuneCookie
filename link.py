import nltk
import numpy as np
import en

korpusFileName = "korpus_final"

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
        if(word == sentence[i][0]):
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
    colSums = mat.sum(axis=0)
    [R,C] = mat.shape
    for colNum in range(C):
        for rowNum in range(R):
            if(colSums[colNum] != 0):
                mat[rowNum][colNum] = mat[rowNum][colNum]/colSums[colNum]
    return mat

def writeInFile(arrayA,arrayB,mat,fileName):
    file = open(fileName, "w")
    file.write(' '.join(arrayA))
    file.write('\n')
    file.write(' '.join(arrayB))
    file.write('\n')
    [R,C] = mat.shape
    for colNum in range(C):
        for rowNum in range(R):
            file.write(str(mat[rowNum][colNum]))
            file.write(" ")
        file.write("\n")
    file.close()

def check_if_more_than_one_nouns(sentence):
    numOfNouns = 0
    for word in sentence:
        if(word[1] == "NN"):
            numOfNouns +=1
    if(numOfNouns > 1):
        return True
    else:
        return False

def pos_to_pers(pron):
    if(pron == "its"):
        return "it"
    elif(pron == "your"):
        return "you"
    elif(pron == "their"):
        return "they"
    elif(pron == "his"):
        return "he"
    elif(pron == "her"):
        return "she"
    elif(pron == "mine"):
        return "i"
    elif(pron == "our"):
        return "us"

##################################################################################



BadSen = []
index = 0
with open(korpusFileName) as f:
	for line in f:
				try:
					index = index+1
					text = nltk.word_tokenize(line)
					text2 = nltk.pos_tag(text)
				except ValueError:
					BadSen.append(index)



numOfPast = 0
numOfPresent = 0
numOfFuture = 0
numOfPlural = 0
numOfPosessive = 0
numOfPersonal = 0

list_part = []
final_list =[]
index = 0
sign = 0
brown_lore_sents = nltk.corpus.brown.tagged_sents(categories=['lore'])
unigram_tagger = nltk.UnigramTagger(brown_lore_sents,cutoff=2,backoff=nltk.DefaultTagger('NN'))


#change "text2 = nltk.pos_tag(text)" to "text2 = unigram_tagger(text)"
with open(korpusFileName) as f:
    for line in f:
        index = index+1
        sign=0
        for i in range(len(BadSen)):
            if index==BadSen[i]:
                sign=1
        if sign==0:
            # print sign,index
            text = nltk.word_tokenize(line)
            sent = nltk.pos_tag(text)
            cp = ["NN","NNS","NNP","NNPS","JJ","JJR","JJS","PRP","PRP$","RB","RBR","RBS","VB","VBD","VBG","VBN","VBP","VBZ"]
            for word in sent:
                if(word[1]==cp[0] or word[1]==cp[2]):                                                                               # Nouns singular
                    list_part.append(tuple([str(word[0]).lower(),cp[0]]))
                elif(word[1]==cp[1] or word[1]==cp[3]):                                                                             # Nouns plural
                    list_part.append(tuple([en.noun.singular(str(word[0]).lower()),cp[0]]))
                elif(word[1]==cp[4] or word[1]==cp[5] or word[1]==cp[6]):                                                           # Adjectives
                    list_part.append(tuple([str(word[0]).lower(),cp[4]]))
                elif(word[1]==cp[7]):                                                                                               # Personal pronouns
                    numOfPersonal += 1
                    list_part.append(tuple([str(word[0]).lower(),cp[7]]))
                elif(word[1]==cp[8]):                                                                                               # Possessive pronouns
                    numOfPosessive += 1
                    list_part.append(tuple([str(word[0]).lower(),cp[8]]))
                    list_part.append(tuple([pos_to_pers(str(word[0]).lower()),cp[8]]))
                elif(word[1] == cp[9] or word[1] == cp[10] or word[1] == cp[11]):                                                   # Adverbs
                    list_part.append(tuple([str(word[0]).lower(),cp[9]]))
                elif(word[1] == cp[12]):
                    list_part.append(tuple([str(word[0]).lower(),cp[12]]))
                elif(word[1] == cp[13] or word[1] == cp[14] or word[1] == cp[15] or word[1] == cp[16] or word[1] == cp[17]):        # Verbs
                    if(word[0]!="'s"):
                        list_part.append(tuple([en.verb.infinitive(str(word[0]).lower()),cp[12]]))
                        if(word[1] == cp[13] or word[1] == cp[15]):
                            numOfPast = numOfPast + 1
                        elif(word[1] == cp[14] or word[1] == cp[16] or word[1] == cp[17]):
                            numOfPresent = numOfPresent + 1
                        else:
                            numOfFuture = numOfFuture + 1
            if len(list_part)!=0:
                final_list.append(list_part)
                list_part = []



'''
final_list = []
list_part = []
test = []
i = 0
with open("test.txt") as f:
    for line in f:
        print(i)
        i += 1
        text = nltk.word_tokenize(line)
        text2 = nltk.pos_tag(text)
        compare_string = nltk.word_tokenize("make dog Twas made raths outgrabe he asks") #ugly but works for now, will look into it more: VB, NN, NNP, VBD, NNS, VBP
        cp = nltk.pos_tag(compare_string)
        for w in range(len(text2)):
            if text2[w][1] is cp[0][1]:
                list_part.append(text2[w])
            elif text2[w][1] is cp[1][1]:
                list_part.append(text2[w])
            elif text2[w][1] is cp[2][1]:
                temp = str(text2[w][0])
                test = [temp, cp[0][1]]
                list_part.append(tuple(test))
            elif text2[w][1] is cp[3][1]:
                print(text2[w][0])
                temp = str(text2[w][0])
                test = [temp, cp[0][1]]
                list_part.append(tuple(test))
            elif text2[w][1] is cp[5][1]:
                test = [text2[w][0], cp[0][1]]
                list_part.append(tuple(test))
            elif text2[w][1] is cp[4][1]:
                numOfPlural += 1
                temp = en.noun.singular(str((text2[w][0])))
                test = [temp, cp[1][1]]
                list_part.append(tuple(test))
            elif text2[w][1] is cp[7][1]:
                numOfPresent += 1
                temp = str(text2[w][0])
                temp  = en.verb.present(temp)
                test = [temp, cp[0][1]]
                list_part.append(tuple(test))

        if len(list_part)!=0:
            final_list.append(list_part)
            list_part = []
            test = []
index = 0
'''




nouns =[]
verbs = []
adjective = []
adverb = []
PosPronouns = []
PersPronouns = []

for sentNum in range(len(final_list)):
    for wordNum in range(len(final_list[sentNum])):
        taggedWord = final_list[sentNum][wordNum]
        if(taggedWord[1] == "NN"):
            nouns.append(taggedWord[0])
        elif(taggedWord[1]=="VB"):
            verbs.append(taggedWord[0])
        elif(taggedWord[1]=="JJ"):
            adjective.append(taggedWord[0])
        elif(taggedWord[1]=="PRP"):
            PersPronouns.append(taggedWord[0])
        elif(taggedWord[1]=="PRP$"):
            PosPronouns.append(taggedWord[0])
        elif(taggedWord[1]=="RB"):
            adverb.append(taggedWord[0])


nouns = getUniqueItems(nouns)
verbs = getUniqueItems(verbs)
adjective = getUniqueItems(adjective)
adverb = getUniqueItems(adverb)
PosPronouns = getUniqueItems(PosPronouns)
PersPronouns = getUniqueItems(PersPronouns)
print(PersPronouns)
print(nouns)




NN = np.zeros((len(nouns),len(nouns)))
NV = np.zeros((len(verbs),len(nouns)))
NAdj = np.zeros((len(adjective),len(nouns)))
NP = np.zeros((len(PersPronouns),len(nouns)))
print(NP.shape)

numOfObjecs = 0
for sentence in final_list:
    if(check_if_more_than_one_nouns(sentence)):
        numOfObjecs=numOfObjecs + 1


for nounNum in range(len(nouns)):
    for sentenceNum in range(len(final_list)):
        posOfWord = checkIfInSentence(nouns[nounNum],final_list[sentenceNum])
        if(posOfWord != -1):
            verbsInSentence = checkForOtherWordtypes("VB",final_list[sentenceNum])
            if(len(verbsInSentence)!=0):
                for word in verbsInSentence:
                    pos = findPosOfWordInArray(word,verbs)
                    if(pos!=None):
                        NV[pos][nounNum] = NV[pos][nounNum]+1
                verbsInSentence = []
            nounsInSentence = checkForOtherWordtypes("NN",final_list[sentenceNum])
            if(len(nounsInSentence)!=0):
                for word in nounsInSentence:
                    pos = findPosOfWordInArray(word,nouns)
                    if(pos!=None):
                        NN[pos][nounNum] = NN[pos][nounNum]+1
                nounsInSentence =[]
            AdjInSentence = checkForOtherWordtypes("JJ",final_list[sentenceNum])
            if(len(AdjInSentence)!=0):
                for word in AdjInSentence:
                    pos = findPosOfWordInArray(word,adjective)
                    if(pos!=None):
                        NAdj[pos][nounNum] = NAdj[pos][nounNum]+1
                AdjInSentence = []
            PersProInSentence = checkForOtherWordtypes("PRP",final_list[sentenceNum])
            if(len(PersProInSentence)!=0):
                for word in PersProInSentence:
                    pos = findPosOfWordInArray(word,PersPronouns)
                    if(pos!=None):
                        NP[pos][nounNum] = NP[pos][nounNum] + 1
                PersProInSentence = []
            PersProInSentence = checkForOtherWordtypes("PRP$",final_list[sentenceNum])
            if(len(PersProInSentence)!=0):
                for word in PersProInSentence:
                    word = pos_to_pers(word)
                    pos = findPosOfWordInArray(word,PersPronouns)
                    if(pos!=None):
                        NP[pos][nounNum] = NP[pos][nounNum] + 1
                PersProInSentence = []

VAdv = np.zeros((len(adverb),len(verbs)))
for verbNum in range(len(verbs)):
    for sentenceNum in range(len(final_list)):
        posOfWord = checkIfInSentence(verbs[verbNum],final_list[sentenceNum])
        if(posOfWord != -1):
            adVerbInSentence = checkForOtherWordtypes("RB",final_list[sentenceNum])
            if(len(adVerbInSentence)!=0):
                for word in adVerbInSentence:
                    pos = findPosOfWordInArray(word,adverb)
                    if(pos!=None):
                        VAdv[pos][verbNum] = VAdv[pos][verbNum]+1
                adVerbInSentence = []



NV = normalizeMatrix(NV)
NN = normalizeMatrix(NN)
NAdj = normalizeMatrix(NAdj)
NP = normalizeMatrix(NP)
VAdv = normalizeMatrix(VAdv)


writeInFile(nouns,verbs,NV,"nouns_verbs.txt")
writeInFile(nouns,nouns,NN,"nouns_nouns.txt")
writeInFile(nouns,adjective,NAdj,"nouns_adjectives.txt")
writeInFile(nouns,PersPronouns,NP,"nouns_pronouns.txt")
writeInFile(verbs,adverb,VAdv,"verbs_adverbs.txt")





sum = numOfFuture+numOfPast+numOfPresent
file = open("features.txt", "w")
file.write("Tense ")
file.write(str(numOfPast/float(sum)))
file.write(" ")
file.write(str(numOfPresent/float(sum)))
file.write(" ")
file.write(str(numOfFuture/float(sum)))
file.write('\n')
file.write("has object ")
file.write(str(numOfObjecs/float(len(final_list))))
file.write(" ")
file.write(str(1-numOfObjecs/float(len(final_list))))
file.close()
