from __future__ import unicode_literals
import spacy
import sys


word = sys.argv[1]

nlp = spacy.load('it_core_news_sm')

articles = {"Masc" : {
        0 : "il",
        1 : "lo",
        2 : "l'"
    },
    "Fem" : {
        0 : "la",
        2 : "l'"
        },
    "Plur": {
        0 : "i",
        1 : "gli",
        2 : "gli",
        3 : "le"
        }
    }   

vowels = ['a', 'e', 'i', 'o', 'u']
cases_for_one = ['z', 'gn', 'ps', 'x']

def get_article(word):
    doc = nlp(word)
    tokens = [x for x in doc][0]
    
    first_letters = word[:2]

    number = tokens.morph.get("Number")
    gender = tokens.morph.get("Gender")


    articles_gender = gender
    case = 0
    if first_letters in ['gn', 'ps'] or first_letters[0] in ['z', 'x']:
        case = 1
    elif first_letters[0] == 's' and first_letters[1] in vowels:
        case = 1
    elif first_letters[0] in vowels:
        case = 2

    if number == ['Plur']:
        key_to_look = 'Plur'
        if gender == ['Fem']:
            case = 3
    else:
        key_to_look = gender[0]
    
    article = articles[key_to_look][case]

    return article

if __name__ == '__main__':
    article = get_article(word)
    print(article)
