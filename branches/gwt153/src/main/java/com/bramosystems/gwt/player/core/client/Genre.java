/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bramosystems.gwt.player.core.client;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class Genre {

    public static String getGenre(int code) {
        String value = null;
        switch(code) {
            case 0:
                value = "Blues";
                break;
            case 1:
                value = "Classic Rock";
                break;
            case 2:
                value = "Country";
                break;
            case 3:
                value = "Dance";
                break;
            case 4:
                value = "Disco";
                break;
            case 5:
                value = "Funk";
                break;
            case 6:
                value = "Grunge";
                /*
07 - Hip-Hop
08 - Jazz
09 - Metal
10 - New Age
11 - Oldies
12 - Other
13 - Pop
14 - R&B
15 - Rap
16 - Reggae
17 - Rock
18 - Techno
19 - Industrial
20 - Alternative
21 - Ska
22 - Death Metal
23 - Pranks
24 - Soundtrack
25 - Euro-Techno
26 - Ambient
27 - Trip-Hop
28 - Vocal
29 - Jazz+Funk
30 - Fusion
31 - Trance
32 - Classical
33 - Instrumental
34 - Acid
35 - House
36 - Game
37 - Sound Clip
38 - Gospel
39 - Noise
40 - Alternative Rock
41 - Bass
43 - Punk
44 - Space
45 - Meditative
46 - Instrumental Pop
47 - Instrumental Rock
48 - Ethnic
49 - Gothic
50 - Darkwave
51 - Techno-Industrial
52 - Electronic
53 - Pop-Folk
54 - Eurodance
55 - Dream
56 - Southern Rock
57 - Comedy
58 - Cult
59 - Gangsta
60 - Top 40
61 - Christian Rap
62 - Pop/Funk
63 - Jungle
64 - Native US
65 - Cabaret
66 - New Wave
67 - Psychedelic
68 - Rave
69 - Showtunes
70 - Trailer
71 - Lo-Fi
72 - Tribal
73 - Acid Punk
74 - Acid Jazz
75 - Polka
76 - Retro
77 - Musical
78 - Rock & Roll
79 - Hard Rock
80 - Folk
81 - Folk-Rock
82 - National Folk
83 - Swing
84 - Fast Fusion
85 - Bebob
86 - Latin
87 - Revival
88 - Celtic
89 - Bluegrass
90 - Avantgarde
91 - Gothic Rock
92 - Progressive Rock
93 - Psychedelic Rock
94 - Symphonic Rock
95 - Slow Rock
96 - Big Band
97 - Chorus
98 - Easy Listening
99 - Acoustic	100 - Humour
101 - Speech
102 - Chanson
103 - Opera
104 - Chamber Music
105 - Sonata
106 - Symphony
107 - Booty Bass
108 - Primus
109 - Porn Groove
110 - Satire
111 - Slow Jam
112 - Club
113 - Tango
114 - Samba
115 - Folklore
116 - Ballad
117 - Power Ballad
118 - Rhythmic Soul
119 - Freestyle
120 - Duet
121 - Punk Rock
122 - Drum Solo
123 - A Cappella
124 - Euro-House
125 - Dance Hall
126 - Goa
127 - Drum & Bass
128 - Club-House
129 - Hardcore
130 - Terror
131 - Indie
132 - BritPop
133 - Negerpunk
134 - Polsk Punk
135 - Beat
136 - Christian Gangsta
137 - Heavy Metal
138 - Black Metal
139 - Crossover
140 - Contemporary Christian
141 - Christian Rock
142 - Merengue
143 - Salsa
144 - Thrash Metal
145 - Anime
146 - JPop
147 - SynthPop
     *
     */
        }
        return value;
    }
}
