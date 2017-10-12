package zh.gamedata.main;

import java.io.File;

/**
 * Created by ceeg on 2017-10-11.
 */
public class PlayerNoImg {
    public static void main(String[] args) {
        String allPlayers = "3007,3103,3118,3173,3248,3252,3253,3272,3324,3331,3332,3333,3347,3380,3400,3404,3407,3415,3512,3513,3515,3520,3523,3527,3531,3532,3533,3598,3601,3602,3607,3608,3621,3624,3632,3643,3650,3653,3660,3663,3704,3706,3707,3708,3709,3710,3715,3717,3721,3723,3724,3731,3741,3744,3745,3748,3750,3752,3754,3765,3818,3820,3821,3822,3824,3826,3831,3832,3834,3836,3837,3842,3843,3844,3845,3847,3860,3927,3928,3929,3930,3931,3932,3933,3934,3943,3944,3948,3952,3954,3956,3959,3960,3962,3966,3971,3982,3983,3989,3990,3992,3995,4023,4052,4129,4130,4135,4136,4139,4141,4145,4152,4154,4158,4160,4163,4175,4179,4190,4203,4244,4245,4246,4247,4285,4286,4287,4288,4290,4293,4294,4296,4298,4300,4301,4304,4305,4306,4309,4313,4314,4325,4333,4354,4371,4387,4388,4389,4390,4391,4468,4469,4471,4472,4473,4474,4475,4477,4478,4479,4481,4482,4483,4484,4485,4486,4487,4488,4489,4493,4497,4498,4499,4502,4507,4527,4559,4561,4563,4564,4610,4612,4613,4614,4615,4617,4618,4621,4622,4623,4624,4626,4627,4628,4631,4632,4633,4636,4638,4642,4644,4646,4647,4648,4649,4651,4660,4682,4694,4714,4716,4717,4718,4719,4720,4722,4723,4724,4725,4726,4728,4729,4731,4748,4749,4750,4753,4754,4757,4759,4764,4766,4770,4771,4775,4786,4794,4795,4800,4840,4883,4884,4886,4888,4889,4890,4891,4892,4893,4894,4895,4896,4897,4898,4899,4901,4902,4904,4905,4906,4911,4912,4913,4915,4916,4920,4922,4923,4932,4937,4942,4966,4968,4987,5000,5007,5008,5009,5010,5012,5013,5014,5015,5016,5017,5018,5019,5020,5021,5023,5024,5025,5054,5055,5057,5059,5060,5062,5064,5065,5066,5068,5069,5071,5072,5073,5074,5077,5079,5082,5083,5087,5094,5095,5096,5098,5099,5102,5105,5106,5111,5133,5142,5146,5150,5152,5153,5154,5155,5156,5157,5158,5159,5160,5161,5162,5163,5164,5165,5185,5186,5187,5189,5190,5191,5192,5193,5194,5195,5196,5197,5199,5201,5202,5204,5206,5209,5210,5213,5214,5216,5217,5220,5222,5225,5233,5235,5237,5238,5245,5249,5253,5256,5262,5282,5292,5293,5294,5295,5316,5317,5318,5319,5320,5321,5322,5324,5325,5326,5327,5328,5329,5330,5331,5334,5335,5336,5339,5340,5341,5343,5344,5347,5350,5351,5352,5354,5356,5357,5359,5370,5372,5374,5376,5383,5385,5388,5393,5408,5414,5417,5419,5428,5432,5433,5434,5464,5465,5466,5467,5468,5469,5470,5471,5472,5473,5474,5475,5476,5477,5478,5479,5480,5481,5482,5483,5484,5485,5487,5489,5490,5492,5493,5494,5496,5497,5498,5500,5501,5503,5506,5521,5522,5523,5524,5526,5528,5536,5544,5582,5583,5593,5598,5599,201595,201979,203133,203136,203540,203580,203585,203798,203893,203897,203902,203906,203909,203910,203912,203919,203921,203928,203933,203946,203950,203956,203963,203967,203996,204037,204054,1626150,20160001,20160002,20160004,20160005,20160008,20160009,20160010,20160012,20160013,20160015,20160017,20160020,20160021,20160022,20160023,20160024,20160026,20160027,20160028,20160029,20160030,20160033,20160035,20160036,20160037,20160039,20160040,20160043,20160044,20160045,20160046,20160047,20160049,20160050,20160052,20160053,20160054,20160055,20160056,20160058,20160059,20160062,20160063,20160064,20160065,20160067,20160068,20160069,20160072,20160074,20160075,20160076,20160078,20160079,20160080,20160081,20160082,20160083,20160085,20160086,20160087,20160088,20160089,20160090,20160091,20160092,20160093,20160094,20160095,20160097,20160100,20160102,20160103,20160106,20160107,20160111,20160112,20160113,20160114,20160115,20160116,20160117,20160119,20160120,20160121,20160122,20160123,20160131,20160134,20160138,20160139,20160142,20160143,20160144,20160145,20160146,20160147,20160152,20160157,20160161,20160162,20160163,20160164,20160165,20160166,20160176,20160178,20160183,20160184,20160185,20160186,20160187,20160188,20160189,20160190,20160192,20160193,20160194,20160195,20160196,20160197,20160198,20160199,20160200,20160201,20160202,20160203,20160204,20160205,20160206,20160207,20160208,20160209,20160210,20160211,20160212,20160213,20160214,20160215,20160216,20160217,20160218,20160219,20160220,20160221,20160222,20160223,20160224,20160225,20160226,20160227,20160228,20160229,20160230,20160231,20160232,20160233,20160234,20160235,20160236,20160237,20160238,20160239,20160240,20160241,20160242,20160243,20160244,20160245,20160246,20160247,20160248,20160249,20160250,20160251,20160252,20160253,20160254,20160255,20160257,20160258,20160259,20160260,20160261,20160262,20160263,20160264,20160265,20160266,20160267,20160268,20160269,20160270,20160271,20160272,20160273,20160274,20160276,20160277,20160278,20160279,20160280,20160281,20160282,20160283,20160284,20160285,20160286,20160287,20160288,20160289,20160290,20160291,20160292,20160293,20160294,20160295,20160296,20160297,20160298,20160299,20160300,20160301,20160302,20160303,20160305,20160306,20160307,20160308,20160309,20160310,20160311,20160312,20160313,20160314,20160315,20160316,20160317,20160318,20160319,20160320,20160321,20160322,20160323,20160324,20160325,20160326,20160327,20160328,20160329,20160330,20160331,20160332,20160333,20160335,20160336,20160337,20160339,20160340,20160341,20160342,20160343,20160344,20160345,20160346,20160347,20160348,20160349,20160355,20160356,20160357,20160358";
        String[] ids = allPlayers.split(",");

        File playerDir = new File("E:\\nbamanager\\app\\style\\images\\player");
        File[] imgs = playerDir.listFiles();

        for (String id : ids) {
            boolean have = false;
            for (File imgFile : imgs) {
                //System.out.println(imgFile.getName());
                if (imgFile.getName().split("\\.")[0].equals(id)) {
                    have = true;
                }
            }
            if (!have) {
                System.out.println(id + ",");
            }
        }
    }
}
