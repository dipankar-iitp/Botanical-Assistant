package com.example.data.sample

import com.example.data.model.BotanicalAnalysisResult
import com.example.data.model.BotanicalMarkdownParser

data class SampleSpecimen(
    val id: String,
    val title: String,
    val scientificName: String,
    val family: String,
    val category: String, // "Medicinal", "Toxic / Hazard", "Digestive", "Aromatic"
    val isToxic: Boolean,
    val shortDescription: String,
    val fullMarkdown: String
) {
    fun toAnalysisResult(): BotanicalAnalysisResult {
        return BotanicalMarkdownParser.parse(fullMarkdown)
    }
}

object SamplePlants {

    val ECHINACEA = SampleSpecimen(
        id = "echinacea",
        title = "Purple Coneflower",
        scientificName = "Echinacea purpurea",
        family = "Asteraceae",
        category = "Immunity",
        isToxic = false,
        shortDescription = "Prominent medicinal flower known for immune stimulation and cold relief.",
        fullMarkdown = """
        **1. Plant Identification**
        * **Common Name(s):** Purple Coneflower, Echinacea, Kansas Snakeroot
        * **Scientific Name:** Echinacea purpurea
        * **Family:** Asteraceae (Daisy / Sunflower family)
        * **Identification Confidence:** High - distinctive central spiny cone with drooping reddish-purple ray florets.

        **2. Visual Analysis**
        * **Observed Features:** Prominent cone-shaped brownish-orange central disc florets; drooping, narrow lanceolate ray petals ranging from rose to deep purple; rough, hairy coarse stems and alternating ovate-lanceolate serrated leaves.

        **3. Historical & Cultural Context**
        * **Origin/Native Region:** Central and eastern North America, thriving in open prairies and dry woodlands.
        * **Cultural Significance:** Extensively used by Indigenous North American peoples including the Plains tribes (Lakota, Cheyenne, Choctaw) for wound poultices, snakebites, and toothaches prior to European introduction.

        **4. Traditional Medicinal & Therapeutic Uses**
        * **Documented Uses:** Immune system modulation, shortening duration and symptoms of upper respiratory infections, soothing sore throat, and topical wound healing.
        * **Active Compounds:** Cichoric acid, Echinacoside, Alkylamides (isobutylamides), Polysaccharides, Flavonoids (quercetin, kaempferol).

        **5. Safety, Toxicity & Precautions (CRITICAL)**
        * **Toxicity:** Non-toxic to humans and common pets (dogs/cats). Look-alikes: Black-eyed Susan (Rudbeckia hirta), which is also generally non-toxic but lacks immune compounds.
        * **Contraindications:** Avoid in individuals with systemic autoimmune diseases (e.g., lupus, multiple sclerosis) or severe Asteraceae/ragweed allergies due to potential hypersensitivity reactions.

        **6. Database JSON Export**
        ```json
        {
          "scientific_name": "Echinacea purpurea",
          "common_names": ["Purple Coneflower", "Echinacea", "Kansas Snakeroot"],
          "confidence_score": "High",
          "toxicity_warning": false
        }
        ```
        """.trimIndent()
    )

    val FOXGLOVE = SampleSpecimen(
        id = "foxglove",
        title = "Foxglove (DEADLY HAZARD)",
        scientificName = "Digitalis purpurea",
        family = "Plantaginaceae",
        category = "Toxic / Hazard",
        isToxic = true,
        shortDescription = "CRITICAL TOXIC LOOKALIKE. Contains potent cardiac glycosides that cause fatal arrhythmias.",
        fullMarkdown = """
        **1. Plant Identification**
        * **Common Name(s):** Common Foxglove, Purple Foxglove, Fairy Thimbles, Dead Man's Bells
        * **Scientific Name:** Digitalis purpurea
        * **Family:** Plantaginaceae (formerly Scrophulariaceae)
        * **Identification Confidence:** High - tall terminal raceme with distinctive tubular campanulate purple spotted flowers.

        **2. Visual Analysis**
        * **Observed Features:** Tall biennial spike (1–2 meters); tubular, nodding, glove-finger-shaped purple-pink corollas with dark spotted white rings inside the throat; large basal rosette of softly hairy, rugose ovate leaves.

        **3. Historical & Cultural Context**
        * **Origin/Native Region:** Native to temperate Western and Southwestern Europe; naturalized widely in the Pacific Northwest and Australasia.
        * **Cultural Significance:** Known in folklore as 'Fairy Thimbles' and associated with witchcraft. In 1785, British physician William Withering famously documented its pharmacological efficacy and lethal dosing for congestive heart dropsy.

        **4. Traditional Medicinal & Therapeutic Uses**
        * **Documented Uses:** Historical precursor to clinical heart failure therapies. NOTE: Raw domestic consumption is STRICTLY PROHIBITED; only purified pharmaceuticals (Digoxin) with exact microgram calibration are safe.
        * **Active Compounds:** Cardiac glycosides (Digoxin, Digitoxin, Gitoxin), Anthraquinones, Flavonoids.

        **5. Safety, Toxicity & Precautions (CRITICAL)**
        * **Toxicity:** HIGHLY LETHAL AND TOXIC to humans, dogs, cats, and livestock! All plant parts contain lethal cardiac toxins. Fatal look-alikes: First-year basal leaves closely resemble Comfrey (Symphytum officinale) and Borage, which has led to fatal accidental foraging poisonings.
        * **Contraindications:** DO NOT INGEST UNDER ANY CIRCUMSTANCES. Ingestion causes nausea, hallucinations, severe bradycardia, ventricular fibrillation, and cardiac arrest. Seek emergency poison control immediately upon suspected exposure.

        **6. Database JSON Export**
        ```json
        {
          "scientific_name": "Digitalis purpurea",
          "common_names": ["Common Foxglove", "Purple Foxglove", "Fairy Thimbles", "Dead Man's Bells"],
          "confidence_score": "High",
          "toxicity_warning": true
        }
        ```
        """.trimIndent()
    )

    val PEPPERMINT = SampleSpecimen(
        id = "peppermint",
        title = "Peppermint",
        scientificName = "Mentha × piperita",
        family = "Lamiaceae",
        category = "Digestive",
        isToxic = false,
        shortDescription = "Aromatic hybrid mint prized worldwide for digestive relief and refreshing aroma.",
        fullMarkdown = """
        **1. Plant Identification**
        * **Common Name(s):** Peppermint, Brandy Mint, Balm Mint
        * **Scientific Name:** Mentha × piperita
        * **Family:** Lamiaceae (Mint family)
        * **Identification Confidence:** High - quadrangular square stem, strongly serrated dark green leaves, intense menthol aroma.

        **2. Visual Analysis**
        * **Observed Features:** Distinctive square, reddish-purple stems; opposite, dark green lanceolate leaves with coarsely toothed serrated margins; terminal spikes of tiny lilac-pink flowers; glandular oil dots on leaf underside.

        **3. Historical & Cultural Context**
        * **Origin/Native Region:** Natural sterile hybrid between Watermint (Mentha aquatica) and Spearmint (Mentha spicata), originating in Europe and the Mediterranean.
        * **Cultural Significance:** Mentioned in ancient Egyptian Ebers Papyrus (1550 BC) and Greek mythology where nymph Minthe was transformed into the fragrant sweet herb.

        **4. Traditional Medicinal & Therapeutic Uses**
        * **Documented Uses:** Spasmolytic relief for irritable bowel syndrome (IBS), dyspepsia, tension headaches (via topical cooling menthol), and nasal decongestion.
        * **Active Compounds:** Menthol (30–55%), Menthone (14–32%), 1,8-Cineole, Menthyl acetate, Rosmarinic acid.

        **5. Safety, Toxicity & Precautions (CRITICAL)**
        * **Toxicity:** Safe for humans in dietary and moderate therapeutic amounts. Pure essential oil is toxic if swallowed undiluted and toxic to cats and dogs (can cause liver strain if concentrated oil is ingested).
        * **Contraindications:** Individuals with severe gastroesophageal reflux disease (GERD) or hiatal hernia should avoid high doses, as menthol relaxes the lower esophageal sphincter, exacerbating heartburn.

        **6. Database JSON Export**
        ```json
        {
          "scientific_name": "Mentha × piperita",
          "common_names": ["Peppermint", "Brandy Mint", "Balm Mint"],
          "confidence_score": "High",
          "toxicity_warning": false
        }
        ```
        """.trimIndent()
    )

    val DEADLY_NIGHTSHADE = SampleSpecimen(
        id = "nightshade",
        title = "Deadly Nightshade (FATAL TOXIN)",
        scientificName = "Atropa belladonna",
        family = "Solanaceae",
        category = "Toxic / Hazard",
        isToxic = true,
        shortDescription = "EXTREMELY POISONOUS. Ingestion of 2-5 glossy black berries can be fatal to children.",
        fullMarkdown = """
        **1. Plant Identification**
        * **Common Name(s):** Deadly Nightshade, Belladonna, Devil's Cherries, Dwayberry
        * **Scientific Name:** Atropa belladonna
        * **Family:** Solanaceae (Nightshade family)
        * **Identification Confidence:** High - bell-shaped dull purple flowers with glossy jet-black berries seated in a star-shaped calyx.

        **2. Visual Analysis**
        * **Observed Features:** Branching herbaceous perennial with dull, dark green ovate leaves in unequal pairs; nodding, solitary bell-shaped flowers of brownish-purple with greenish veins; single, shiny spherical black berries (approx. 1 cm) backed by a 5-lobed persistent star calyx.

        **3. Historical & Cultural Context**
        * **Origin/Native Region:** Native to Southern, Central, and Eastern Europe, North Africa, and Western Asia.
        * **Cultural Significance:** Renaissance Venetian ladies applied its juice to pupil dilators for seductive allure ('bella donna' = beautiful lady). Weaponized as poison in Roman imperial court conspiracies and Macbeth's Scottish armies against the Danes.

        **4. Traditional Medicinal & Therapeutic Uses**
        * **Documented Uses:** Foundation for modern anticholinergic and antimuscarinic pharmacology. Modern medicine synthesizes pure atropine for bradycardia resuscitation and ophthalmology. NEVER used as raw folk medicine today.
        * **Active Compounds:** Tropane alkaloids: Atropine, Scopolamine (hyoscine), Hyoscyamine.

        **5. Safety, Toxicity & Precautions (CRITICAL)**
        * **Toxicity:** DEADLY TOXIC! One of the most hazardous flora in the world. As few as 2 to 4 sweet-tasting berries can kill a human child; 10–20 can kill an adult. Highly toxic to domestic pets. Dangerous look-alikes: Black nightshade (Solanum nigrum) and blueberries/blackberries to untrained eyes.
        * **Contraindications:** STRICT INGESTION PROHIBITION. Symptoms follow classic anticholinergic toxidrome: severe dry mouth, extreme mydriasis, tachycardia, urinary retention, violent hallucinations, delirium, hyperthermia, and respiratory collapse.

        **6. Database JSON Export**
        ```json
        {
          "scientific_name": "Atropa belladonna",
          "common_names": ["Deadly Nightshade", "Belladonna", "Devil's Cherries", "Dwayberry"],
          "confidence_score": "High",
          "toxicity_warning": true
        }
        ```
        """.trimIndent()
    )

    val ST_JOHNS_WORT = SampleSpecimen(
        id = "st_johns_wort",
        title = "St. John's Wort",
        scientificName = "Hypericum perforatum",
        family = "Hypericaceae",
        category = "Mood & Nervous",
        isToxic = false,
        shortDescription = "Golden yellow herb with translucent leaf perforations, used for mild-to-moderate depression.",
        fullMarkdown = """
        **1. Plant Identification**
        * **Common Name(s):** St. John's Wort, Klamath Weed, Goatweed, Amber Touch-and-Heal
        * **Scientific Name:** Hypericum perforatum
        * **Family:** Hypericaceae
        * **Identification Confidence:** High - translucent punctate glands on leaves when held to light, five bright yellow petals with black margin glands.

        **2. Visual Analysis**
        * **Observed Features:** Bright golden-yellow 5-petaled flowers with abundant protruding stamens; black marginal glandular dots that stain red when crushed; opposite, sessile oblong leaves displaying transparent pinhole glands (perforations) when backlit.

        **3. Historical & Cultural Context**
        * **Origin/Native Region:** Native to temperate Eurasia and North Africa; naturalized in the Americas and Australia.
        * **Cultural Significance:** Harvested traditionally on St. John's Eve (June 23) and hung over doorways in medieval Europe to ward off evil spirits, melancholia, and lightning.

        **4. Traditional Medicinal & Therapeutic Uses**
        * **Documented Uses:** Clinically validated for mild to moderate depressive episodes; soothing nerve pain (sciatica, neuralgia); topical red-oil infusion for burns and superficial wound recovery.
        * **Active Compounds:** Hypericin, Pseudohypericin, Hyperforin (reuptake inhibitor), Flavonoids (hyperoside, isoquercitrin).

        **5. Safety, Toxicity & Precautions (CRITICAL)**
        * **Toxicity:** Moderate phototoxicity in fair-skinned individuals or grazing livestock upon heavy sun exposure (hypericism). Toxic to livestock in large grazing volumes.
        * **Contraindications:** CRITICAL DRUG INTERACTIONS! Potent inducer of Cytochrome P450 enzyme CYP3A4 and P-glycoprotein. Dramatically reduces efficacy of oral contraceptives, immunosuppressants (cyclosporine), anticoagulants (warfarin), and antiretrovirals. Severe risk of Serotonin Syndrome if co-administered with SSRIs or MAOIs.

        **6. Database JSON Export**
        ```json
        {
          "scientific_name": "Hypericum perforatum",
          "common_names": ["St. John's Wort", "Klamath Weed", "Goatweed", "Amber Touch-and-Heal"],
          "confidence_score": "High",
          "toxicity_warning": false
        }
        ```
        """.trimIndent()
    )

    val GINKGO = SampleSpecimen(
        id = "ginkgo",
        title = "Ginkgo Biloba",
        scientificName = "Ginkgo biloba",
        family = "Ginkgoaceae",
        category = "Cognitive",
        isToxic = false,
        shortDescription = "Prehistoric living fossil with unique fan-shaped leaves, supporting cerebral microcirculation.",
        fullMarkdown = """
        **1. Plant Identification**
        * **Common Name(s):** Ginkgo, Maidenhair Tree, Silver Apricot
        * **Scientific Name:** Ginkgo biloba
        * **Family:** Ginkgoaceae
        * **Identification Confidence:** High - unmistakable two-lobed fan-shaped leaves with dichotomous venation.

        **2. Visual Analysis**
        * **Observed Features:** Distinctive fan-shaped leathery leaves with parallel, radiating veins; notch dividing blade into two lobes; bright golden-yellow autumnal coloration; fleshy apricot-like seed coating on female trees producing pungent butyric acid aroma.

        **3. Historical & Cultural Context**
        * **Origin/Native Region:** Native to Zhejiang province in Eastern China; widely cultivated globally.
        * **Cultural Significance:** The sole surviving member of an ancient division dating back over 270 million years (Permian period). Revered in Chinese and Japanese temple courtyards as a symbol of longevity and vitality.

        **4. Traditional Medicinal & Therapeutic Uses**
        * **Documented Uses:** Standardized extract (EGb 761) used for cognitive decline, cerebral vascular insufficiency, intermittent claudication, and peripheral microcirculatory support.
        * **Active Compounds:** Ginkgolides (A, B, C), Bilobalide (terpene lactones), Flavonoid glycosides (quercetin, kaempferol, isorhamnetin).

        **5. Safety, Toxicity & Precautions (CRITICAL)**
        * **Toxicity:** Standardized leaf extract is safe. However, raw ginkgo seeds contain Ginkgotoxin (4'-O-methylpyridoxine) which can induce fatal convulsions in children. The fleshy outer sarcotesta causes severe contact dermatitis (urushiol-like).
        * **Contraindications:** Inhibits platelet activating factor (PAF); discontinue at least two weeks before scheduled surgery. Use extreme caution when taken alongside anticoagulants or antiplatelet agents (aspirin, warfarin).

        **6. Database JSON Export**
        ```json
        {
          "scientific_name": "Ginkgo biloba",
          "common_names": ["Ginkgo", "Maidenhair Tree", "Silver Apricot"],
          "confidence_score": "High",
          "toxicity_warning": false
        }
        ```
        """.trimIndent()
    )

    val ALL = listOf(ECHINACEA, FOXGLOVE, PEPPERMINT, DEADLY_NIGHTSHADE, ST_JOHNS_WORT, GINKGO)
}
