#!/usr/bin/python

import json
f = open('herostats.json','r')
data = json.loads(f.read())
f.close()
f = open('src/mechanics/Hero.java','w')
f.write('package mechanics;\n\npublic class Hero{\n')
attributes = [
	'strength',
	'agillity',
	'intelligence',
	'hp',
	'hp_regen',
	'mana',
	'mana_regen',
	'armor',
	'attack_speed',
	'move_speed',
	'damage',
]
f.write('\tpublic String name;\n')
f.write('\tpublic double current_hp;\n')
f.write('\tpublic double current_mana;\n')
for a in attributes:
	f.write('\tpublic double '+a+';\n')
f.write('\tpublic Hero(')
for a in attributes:
	f.write('double '+a+',')
f.write('String name){\n')
f.write('\t\tthis.name=name;\n')
for a in attributes:
	f.write('\t\tthis.'+a+'='+a+';\n')
f.write('\t\tthis.current_hp=hp;\n')
f.write('\t\tthis.current_mana=mana;\n')
f.write('\t}\n')

for i in data:
	name = data[i]['Name']
	name = name.replace(' ','_').replace("'",'').replace('-','')
	mindmg = data[i]['MinDmg']
	maxdmg = data[i]['MaxDmg']
	move_speed = data[i]['Movespeed']
	strength = data[i]['BaseStr'] + 25 * data[i]['StrGain']
	agillity = data[i]['BaseAgi'] + 25 * data[i]['AgiGain']
	intelligence = data[i]['BaseInt'] + 25 * data[i]['IntGain']
	hp = data[i]['HP'] + strength * 19
	hp_regen = data[i]['HPRegen'] + strength * 0.03
	mana = data[i]['Mana'] + intelligence * 13
	mana_regen = data[i]['ManaRegen'] + intelligence * 0.04
	armor = data[i]['Armor'] + agillity * 0.14
	attack_speed = (100 + agillity) * 0.01 / data[i]['BaseAttackTime']
	if data[i]['PrimaryStat'] == 0:
		mindmg += strength
		maxdmg += strength
	elif data[i]['PrimaryStat'] == 1:
		mindmg += agillity
		maxdmg += agillity
	elif data[i]['PrimaryStat'] == 2:
		mindmg += intelligence
		maxdmg += intelligence
#	print "%20s %4.1f %4d %4.2f %4.2f" % (name,maxdmg,hp,hp_regen,attack_speed) 
	f.write('\tpublic static Hero '+name+' = new Hero('+
		str(strength)+','+
		str(agillity)+','+
		str(intelligence)+','+
		str(hp)+','+
		str(hp_regen)+','+
		str(mana)+','+
		str(mana_regen)+','+
		str(armor)+','+
		str(attack_speed)+','+
		str(move_speed)+','+
		str(maxdmg)+','+
		'"'+name+'");\n')

f.write('}')
f.close()
