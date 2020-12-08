/**
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.blockchain.data.stash.parse;


import com.webank.blockchain.data.stash.block.BlockHeader;
import com.webank.blockchain.data.stash.rlp.ByteUtil;
import com.webank.blockchain.data.stash.rlp.DecodeResult;
import com.webank.blockchain.data.stash.rlp.RLP;
import com.webank.blockchain.data.stash.rlp.RLPList;
import com.webank.blockchain.data.stash.utils.JsonUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.HexUtil;

/**
 * RlpTest
 *
 * @Description: RlpTest
 * @author maojiayu
 * @data Sep 3, 2019 10:55:33 AM
 *
 */
public class RlpTest {

    public static void main(String[] args) {
        /*
        String s =
                "f9079ff902bfa032c6ceb53e462a5a6225a0c75ce81f4eebd1b3a7a6508069f69af06b9f5bfceda01113d3ac595cc4e5dac26c169631d27aa4d60b02102ee3226aacd42aeecc403fa06509eecf50ad23dd492136865553d8d21eabed4d78cae9701c0b59d62629e47fa036c3b68691ee66aff93d37f218f02402b9eef1dde915a27f2a1469ef90cbf784a01113d3ac595cc4e5dac26c169631d27aa4d60b02102ee3226aacd42aeecc403fb901000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003808086016c51aff57fc080f90108b8406b69fdd22a463ed5f002925a803aa5d6a744d9da6aead4a6167ca88787777d99c406ce77fc3aa3ac14dbe25c2b858e2aad9528dd2922ab78cb11186e5acba922b840818f3f239d5a5395cf5d9080fc0a62940ef607be57eeb702eb5304edfa203cc6c59998aaa6f1e06adb0441341f6204420b9fb146ac7932c1e63b8867e27766e6b840b71adb5cc8a6c9379787204dff60648392b5020a80c669991f041ee2db2389b20392a0e46e344fc9229a3724d546ca4f2d20aa5b0ed3226fd1abd8644ee9a725b840c2d6430ed0346eb4c166f999a0faad1bbe2ed1a8a550dbbc1f8c0c8c3a43d73e528c1c634aa9bac30161e72122f2ee3f4b568a18a3fe4ea3b5b5a4401ed5f8afb90280010000000000000074020000f90271a002b44129da1374c56e9f3574557f8da91d8e100ad629a31a7babbcb8717039fc8411e1a3008411e1a30082025a94000000000000000000000000000000000000100280b901e42dca76c100000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000e00000000000000000000000000000000000000000000000000000000000000120000000000000000000000000000000000000000000000000000000000000016000000000000000000000000000000000000000000000000000000000000001c00000000000000000000000000000000000000000000000000000000000000006745f64656d6f00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005667275697400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000167b226974656d5f6e616d65223a226f72616e6765227d00000000000000000000000000000000000000000000000000000000000000000000000000000000002c7b226974656d5f6964223a7b226571223a2231227d2c226e616d65223a7b226571223a226672756974227d7d000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000101801ba0602ec8698d4fc8e3bcbb911109536a6147b32973b88148f33c4405f8ffe0005aa061c318f180344507e608e78dbc861647d58075339c3a328492d754fb43b78ae5a0be75bbeb1aac1f2c9c614b176589fc2306f05572fc369356075d76619a0b2dd6f8d2f84403b841583e7b100fe5e4c45a00b4ef06ce3f5d00fd4658f44edad2013731a231dfe51874faca7d236543e1b70028eeda1d69eede9586db0042dbefb84744a37b78f01801f84480b8417f911646e037af67bd637aaacf9b06886301ec6747578dbef050e50d53110ed3161ba2f5df8d9f83ad22190771b774b70f494753a3417a50d9f3d4792a44799c01f84402b841f0b3f72970c9cd6c8aa9d4b7f07813b1ef5e25b4afb47475a7678a33546a27a95335a51b6549ddab1da396e1907bc92cb1f217b10b2fe5177afe0816b0aad8b800f90162f9015fa01113d3ac595cc4e5dac26c169631d27aa4d60b02102ee3226aacd42aeecc403f8270d8940000000000000000000000000000000000000000b901000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080a00000000000000000000000000000000000000000000000000000000000000001c0";
        String s0 =
                "f90403f903dca00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000000b901000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080808086016bea430060f90225b90222312d62373161646235636338613663393337393738373230346466663630363438333932623530323061383063363639393931663034316565326462323338396232303339326130653436653334346663393232396133373234643534366361346632643230616135623065643332323666643161626438363434656539613732352c36623639666464323261343633656435663030323932356138303361613564366137343464396461366165616434613631363763613838373837373737643939633430366365373766633361613361633134646265323563326238353865326161643935323864643239323261623738636231313138366535616362613932322c63326436343330656430333436656234633136366639393961306661616431626265326564316138613535306462626331663863306338633361343364373365353238633163363334616139626163333031363165373231323266326565336634623536386131386133666534656133623562356134343031656435663861662c38313866336632333964356135333935636635643930383066633061363239343065663630376265353765656237303265623533303465646661323033636336633539393938616161366631653036616462303434313334316636323034343230623966623134366163373933326331653633623838363765323737363665362c2d706266742d73746f726167652d313030302d33303030303030303080c080a0e50403bd5d4cb29660e54ee59b5c5b24a6d7397dbe436ade5248018f07cfbf82c0c0";
        byte[] b = HexUtil.decodeHex(s);
        DecodeResult result = RLP.decode(HexUtil.decode(s), 0);
        DecodeResult result1 = RLP.decode(b, 1);

        Console.log(JsonUtils.toJson(result.getDecoded()));
        Console.log(JsonUtils.toJson(result1.getDecoded()));

        String len = "\"AQ==\"";
        System.out.println(RLP.decodeLong(len.getBytes(), 0));
        System.out.println(RLP.decode2(len.getBytes()));
        System.out.println(ByteUtil.byteArrayToLong(len.getBytes()));

        RLPList params = RLP.decode2(b);
        System.out.println(params.size());
        RLPList block = (RLPList) params.get(0);
        System.out.println(JsonUtils.toJson(block));
        // Parse Header
        RLPList header = (RLPList) block.get(0);
        System.out.println(header.size());
        System.out.println(JsonUtils.toJson(header));
        BlockHeader h = new BlockHeader(header);

        System.out.println(JsonUtils.toJson(h));
        System.out.println(DateUtil.date(1564738576749L));

        RLPList params0 = RLP.decode2(HexUtil.decodeHex(s0));
        RLPList header0 = (RLPList) ((RLPList) params0.get(0)).get(0);
        BlockHeader h0 = new BlockHeader(header0);
        System.out.println(JsonUtils.toJson(h0));

         */
    }

}
