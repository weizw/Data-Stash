/**
 * Copyright 2020 Webank.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.blockchain.data.stash.verify;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.webank.blockchain.data.stash.block.BlockV2RC2;
import com.webank.blockchain.data.stash.crypto.CyptoInterface;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webank.blockchain.data.stash.block.BlockV2RC2;
import com.webank.blockchain.data.stash.crypto.CyptoInterface;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.HexUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * BlockValidator
 *
 * @Description: BlockValidator
 * @author maojiayu
 * @data Sep 11, 2019 10:03:24 PM
 *
 */
@Slf4j
@Service
public class BlockValidator {
    @Autowired
    private CyptoInterface cyptoInterface;

    public boolean validateTransaction(BlockV2RC2 block) {
        if (block.getBlockHeader().getNumber().compareTo(BigInteger.ZERO) <= 0) {
            return true;
        }
        String txRoot = "0x" + HexUtil.encodeHexStr(block.getBlockHeader().getTransactionsRoot());
        log.debug("TxRoot is {}", txRoot);
        String checkedRoot = HexUtil.encodeHexStr(cyptoInterface.hash(block.getTransactions()));
        log.debug("Check root is {}", checkedRoot);
        if (txRoot.equalsIgnoreCase(checkedRoot)) {
            return true;
        } else {
            //throw new ValidateException("Check transaction root error.");
            return false;
        }
    }

    public boolean validateSigList(BlockV2RC2 block) throws Exception {
        List<Map<String, String>> sigs = block.getSigList();
        byte[] hash = block.getHash();
        List<byte[]> sealerList = block.getBlockHeader().getSealerList();
        for (Map<String, String> sig : sigs) {
            for (Entry<String, String> entry : sig.entrySet()) {
                String signature = entry.getValue();
                int index = new BigInteger(entry.getKey(), 16).intValue();
                byte[] sealerNodeKey = sealerList.get(index);
                if(sealerNodeKey.length == 64){
                    byte[] standardNodeKey = new byte[65];
                    standardNodeKey[0] = 0x04;
                    System.arraycopy(sealerNodeKey, 0, standardNodeKey, 1, 64);
                    sealerNodeKey = standardNodeKey;
                }
                if (!cyptoInterface.verify(hash, signature, Hex.encodeHexString(sealerNodeKey))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        String s =
                "f921fef902bfa0505ad4a9423fb58abadb7307b5de209279a37765c314564329ef9c541369aa04a093dada2f0d756614e15597c6eb12dbd879fbcc89cc008287d7ffa04969396b18a03a81e50dce8c848991cfa00d69c728591511411cb8af35312f58ea9a9d44ef44a07326b76226eef3b3e867cba83e94bb2ca90936732a0f53b35fa04ab619556177a093dada2f0d756614e15597c6eb12dbd879fbcc89cc008287d7ffa04969396b18b9010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000168080860175536927bec002f90108b8401e25dae2aa8caecb99d678071da0099f2729646cd136800c0bd90b7d674d1937f80bc1b2264f3bbe9e1fcf0275968c259103096b69a35b6980ced7b2e6fe70bbb84034a5d2e91124fe7dafbd2caa19406e733d52322d834f77fbfc6e7bc0b45053449c2c5199d3e27b0a9632eafe0bb6c8dfd4182999b3165677054069b5fb1717b1b84063b933039e7ec6d05114338b039d2a3107d201c9c5a85277dc9634e4c1f5dbb25fcadaf73c850e47ffd86180d733186d0b61107c5838de527c74536f7a3eb244b840ac76607b1fe52df0e9d15b3f70373c541aba0854e65f26e7cf0171fccfb6b801643d576f847fabf83e243e52a225f662327ba3c40d855ffaa5c4905fcfc5178fb91c420100000000000000361c0000f91c33a0011c5fa8a93aae3d0b48cf7a126c14112b5ba594694cf3d72de79a4ea8eb951d85051f4d5c0083419ce08202098080b91bba60806040523480156200001157600080fd5b5060405162001b3a38038062001b3a8339810180604052620000379190810190620001fe565b33600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600181905550806003908051906020019062000097929190620000db565b507f96cbce3c2f7574181c59fe1f42ee3733c5ebb6ff910ad138ec2877995da00b883382604051620000cb929190620002a5565b60405180910390a15050620003b0565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200011e57805160ff19168380011785556200014f565b828001600101855582156200014f579182015b828111156200014e57825182559160200191906001019062000131565b5b5090506200015e919062000162565b5090565b6200018791905b808211156200018357600081600090555060010162000169565b5090565b90565b60006200019882516200035f565b905092915050565b600082601f8301121515620001b457600080fd5b8151620001cb620001c58262000307565b620002d9565b91508082526020830160208301858383011115620001e857600080fd5b620001f583828462000369565b50505092915050565b600080604083850312156200021257600080fd5b600062000222858286016200018a565b925050602083015167ffffffffffffffff8111156200024057600080fd5b6200024e85828601620001a0565b9150509250929050565b62000263816200033f565b82525050565b6000620002768262000334565b8084526200028c81602086016020860162000369565b62000297816200039f565b602085010191505092915050565b6000604082019050620002bc600083018562000258565b8181036020830152620002d0818462000269565b90509392505050565b6000604051905081810181811067ffffffffffffffff82111715620002fd57600080fd5b8060405250919050565b600067ffffffffffffffff8211156200031f57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b60005b83811015620003895780820151818401526020810190506200036c565b8381111562000399576000848401525b50505050565b6000601f19601f8301169050919050565b61177a80620003c06000396000f3006080604052600436106100c5576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633d3b51e2146100ca578063479ba9a3146101075780635c803d921461014457806362ab1a771461018157806368895979146101ac5780637c1bf3c5146101d7578063806a0b7214610214578063ae638e061461023f578063bb39513a1461026a578063da359dc814610295578063df94431c146102d2578063f5a12f98146102fb578063f9ce95fe14610326575b600080fd5b3480156100d657600080fd5b506100f160048036036100ec9190810190610e96565b61033d565b6040516100fe91906112f3565b60405180910390f35b34801561011357600080fd5b5061012e60048036036101299190810190610e55565b610491565b60405161013b9190611359565b60405180910390f35b34801561015057600080fd5b5061016b60048036036101669190810190610f56565b61057a565b60405161017891906112af565b60405180910390f35b34801561018d57600080fd5b506101966105b8565b6040516101a391906112af565b60405180910390f35b3480156101b857600080fd5b506101c16105de565b6040516101ce9190611491565b60405180910390f35b3480156101e357600080fd5b506101fe60048036036101f99190810190610f56565b6105e7565b60405161020b9190611491565b60405180910390f35b34801561022057600080fd5b50610229610636565b6040516102369190611374565b60405180910390f35b34801561024b57600080fd5b506102546106d4565b604051610261919061144f565b60405180910390f35b34801561027657600080fd5b5061027f610772565b60405161028c9190611315565b60405180910390f35b3480156102a157600080fd5b506102bc60048036036102b79190810190610e96565b6107dc565b6040516102c99190611396565b60405180910390f35b3480156102de57600080fd5b506102f960048036036102f49190810190610ed7565b610837565b005b34801561030757600080fd5b506103106108ac565b60405161031d91906113ef565b60405180910390f35b34801561033257600080fd5b5061033b6108b2565b005b60606008826040518082805190602001908083835b6020831015156103775780518252602082019150602081019050602083039250610352565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020805480602002602001604051908101604052809291908181526020016000905b82821015610486578382906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104725780601f1061044757610100808354040283529160200191610472565b820191906000526020600020905b81548152906001019060200180831161045557829003601f168201915b5050505050815260200190600101906103ca565b505050509050919050565b6000600182511115156104d9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104d090611471565b60405180910390fd5b8160088360008151811015156104eb57fe5b906020019060200201516040518082805190602001908083835b60208310151561052a5780518252602082019150602081019050602083039250610505565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902090805190602001906105709291906108b4565b5060019050919050565b60078181548110151561058957fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008054905090565b6000600182016000819055507faca9a02cfe513f3f88c54a860469369849c8fa0a2119a8d1f3f75c67ac0c954733836040516106249291906112ca565b60405180910390a16000549050919050565b60048054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106cc5780601f106106a1576101008083540402835291602001916106cc565b820191906000526020600020905b8154815290600101906020018083116106af57829003601f168201915b505050505081565b60038054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561076a5780601f1061073f5761010080835404028352916020019161076a565b820191906000526020600020905b81548152906001019060200180831161074d57829003601f168201915b505050505081565b61077a610914565b610782610914565b604080519081016040528060018152602001600281525090507f84712d7e2b783e5ec8a0f0f50d302da0b15d078ac64968245cfc91e5b7481c988160056040516107cd929190611330565b60405180910390a18091505090565b60607fa0f5dc8c800a837e0a7c88af15df4a194b8cf2b9a1e0d4e1a28013471713ae136004836040516108109291906113b8565b60405180910390a1816004908051906020019061082e929190610936565b50819050919050565b8260018190555081600790805190602001906108549291906109b6565b50806003908051906020019061086b929190610a40565b507f487b33fb5d5ff951b71eea40323f9d8dac61f7aefb4bc9c23399aa9cd900ba2683838360405161089f9392919061140a565b60405180910390a1505050565b60015481565b565b828054828255906000526020600020908101928215610903579160200282015b828111156109025782518290805190602001906108f2929190610ac0565b50916020019190600101906108d4565b5b5090506109109190610b40565b5090565b6040805190810160405280600290602082028038833980820191505090505090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061097757805160ff19168380011785556109a5565b828001600101855582156109a5579182015b828111156109a4578251825591602001919060010190610989565b5b5090506109b29190610b6c565b5090565b828054828255906000526020600020908101928215610a2f579160200282015b82811115610a2e5782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550916020019190600101906109d6565b5b509050610a3c9190610b91565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a8157805160ff1916838001178555610aaf565b82800160010185558215610aaf579182015b82811115610aae578251825591602001919060010190610a93565b5b509050610abc9190610b6c565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610b0157805160ff1916838001178555610b2f565b82800160010185558215610b2f579182015b82811115610b2e578251825591602001919060010190610b13565b5b509050610b3c9190610b6c565b5090565b610b6991905b80821115610b655760008181610b5c9190610bd4565b50600101610b46565b5090565b90565b610b8e91905b80821115610b8a576000816000905550600101610b72565b5090565b90565b610bd191905b80821115610bcd57600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905550600101610b97565b5090565b90565b50805460018160011615610100020316600290046000825580601f10610bfa5750610c19565b601f016020900490600052602060002090810190610c189190610b6c565b5b50565b6000610c2882356116b9565b905092915050565b600082601f8301121515610c4357600080fd5b8135610c56610c51826114d9565b6114ac565b91508181835260208401935060208101905083856020840282011115610c7b57600080fd5b60005b83811015610cab5781610c918882610c1c565b845260208401935060208301925050600181019050610c7e565b5050505092915050565b600082601f8301121515610cc857600080fd5b8135610cdb610cd682611501565b6114ac565b9150818183526020840193506020810190508360005b83811015610d215781358601610d078882610d2b565b845260208401935060208301925050600181019050610cf1565b5050505092915050565b600082601f8301121515610d3e57600080fd5b8135610d51610d4c82611529565b6114ac565b91508082526020830160208301858383011115610d6d57600080fd5b610d788382846116ed565b50505092915050565b600082601f8301121515610d9457600080fd5b8135610da7610da282611555565b6114ac565b91508082526020830160208301858383011115610dc357600080fd5b610dce8382846116ed565b50505092915050565b6000610de382356116d9565b905092915050565b600082601f8301121515610dfe57600080fd5b8135610e11610e0c82611581565b6114ac565b91508082526020830160208301858383011115610e2d57600080fd5b610e388382846116ed565b50505092915050565b6000610e4d82356116e3565b905092915050565b600060208284031215610e6757600080fd5b600082013567ffffffffffffffff811115610e8157600080fd5b610e8d84828501610cb5565b91505092915050565b600060208284031215610ea857600080fd5b600082013567ffffffffffffffff811115610ec257600080fd5b610ece84828501610d81565b91505092915050565b600080600060608486031215610eec57600080fd5b6000610efa86828701610dd7565b935050602084013567ffffffffffffffff811115610f1757600080fd5b610f2386828701610c30565b925050604084013567ffffffffffffffff811115610f4057600080fd5b610f4c86828701610deb565b9150509250925092565b600060208284031215610f6857600080fd5b6000610f7684828501610e41565b91505092915050565b610f8881611679565b82525050565b6000610f99826115ed565b808452602084019350610fab836115ad565b60005b82811015610fdd57610fc1868351610f7f565b610fca82611645565b9150602086019550600181019050610fae565b50849250505092915050565b6000610ff4826115f8565b8084526020840193508360208202850161100d856115ba565b60005b84811015611046578383038852611028838351611132565b925061103382611652565b9150602088019750600181019050611010565b508196508694505050505092915050565b61106081611603565b611069826115c7565b60005b8281101561109b5761107f8583516112a0565b6110888261165f565b915060208501945060018101905061106c565b5050505050565b6110ab8161160e565b6110b4826115d1565b60005b828110156110e6576110ca8583546112a0565b6110d38261166c565b91506020850194506001810190506110b7565b5050505050565b6110f681611699565b82525050565b600061110782611624565b80845261111b8160208601602086016116fc565b6111248161172f565b602085010191505092915050565b600061113d82611619565b8084526111518160208601602086016116fc565b61115a8161172f565b602085010191505092915050565b60008154600181166000811461118557600181146111a5576111e6565b607f600283041680865260ff1983166020870152604086019350506111e6565b600282048086526020860195506111bb856115db565b60005b828110156111dd578154818901526001820191506020810190506111be565b80880195505050505b505092915050565b6111f7816116a5565b82525050565b60006112088261163a565b80845261121c8160208601602086016116fc565b6112258161172f565b602085010191505092915050565b600061123e8261162f565b8084526112528160208601602086016116fc565b61125b8161172f565b602085010191505092915050565b6000601a82527f4279746573206172726179206973206c657373207468616e20320000000000006020830152604082019050919050565b6112a9816116af565b82525050565b60006020820190506112c46000830184610f7f565b92915050565b60006040820190506112df6000830185610f7f565b6112ec60208301846112a0565b9392505050565b6000602082019050818103600083015261130d8184610fe9565b905092915050565b600060408201905061132a6000830184611057565b92915050565b60006080820190506113456000830185611057565b61135260408301846110a2565b9392505050565b600060208201905061136e60008301846110ed565b92915050565b6000602082019050818103600083015261138e8184611132565b905092915050565b600060208201905081810360008301526113b081846110fc565b905092915050565b600060408201905081810360008301526113d28185611168565b905081810360208301526113e681846110fc565b90509392505050565b600060208201905061140460008301846111ee565b92915050565b600060608201905061141f60008301866111ee565b81810360208301526114318185610f8e565b9050818103604083015261144581846111fd565b9050949350505050565b600060208201905081810360008301526114698184611233565b905092915050565b6000602082019050818103600083015261148a81611269565b9050919050565b60006020820190506114a660008301846112a0565b92915050565b6000604051905081810181811067ffffffffffffffff821117156114cf57600080fd5b8060405250919050565b600067ffffffffffffffff8211156114f057600080fd5b602082029050602081019050919050565b600067ffffffffffffffff82111561151857600080fd5b602082029050602081019050919050565b600067ffffffffffffffff82111561154057600080fd5b601f19601f8301169050602081019050919050565b600067ffffffffffffffff82111561156c57600080fd5b601f19601f8301169050602081019050919050565b600067ffffffffffffffff82111561159857600080fd5b601f19601f8301169050602081019050919050565b6000602082019050919050565b6000602082019050919050565b6000819050919050565b6000819050919050565b60008160005260206000209050919050565b600081519050919050565b600081519050919050565b600060029050919050565b600060029050919050565b600081519050919050565b600081519050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b6000602082019050919050565b6000602082019050919050565b6000600182019050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008115159050919050565b6000819050919050565b6000819050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000819050919050565b82818337600083830152505050565b60005b8381101561171a5780820151818401526020810190506116ff565b83811115611729576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a723058205c6daaac747c436743c1b9e0037ff84f35d449ad70c0745fa9218e3a5744e2c36c6578706572696d656e74616cf5003700000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000000574657374320000000000000000000000000000000000000000000000000000000101801ba09ed0ea972c2df11d0c6cc898e2ba8e8b313565464d76aaa2c985fc6c7c756b6aa00ad6739089ce5cba44b577c048ac6d3619064e6c4a84def0c3c97e44ba89c420a085621e42f7cab5d92ee2e816579af3091931c37b6e8f9b2d68525fb31b39d86bf8d2f84402b841559e38a3208bb0a33fbe18224129b2e47ae41fdf1b8e21f234b4cafbf90d100468f92bb98f93398832bd34a5e93154ea3532770c0519d30187af49e3304913c801f84480b841d5827bb33c41537bc682cdc4db4d2b86a3445544b6163601f8528f96d3ec64314f6b594bcec5b573fa0dda3984239d361e38cca9311755f5ba4814d2dded3a8700f84403b8412f0968cf33ffdd1837c905e53f70cf9d4f556561bf80d07e9640385eba7ef7d860dd3ec9bf224a5451ed6c1249c4ea78a8a8c824ff90a9d9cd07f52a438723ab00f901fff901fca093dada2f0d756614e15597c6eb12dbd879fbcc89cc008287d7ffa04969396b18831b0c249450709fae895b65c13283d3609ae2f9b9458e114cb90100000000000000000000000001000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000000000000008080f8bbf8b99450709fae895b65c13283d3609ae2f9b9458e114ce1a096cbce3c2f7574181c59fe1f42ee3733c5ebb6ff910ad138ec2877995da00b88b88000000000000000000000000082e7e2d644677356fae77820189c597e15b973c3000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000057465737432000000000000000000000000000000000000000000000000000000";
        BlockV2RC2 block = new BlockV2RC2(s);
        Console.log(new BlockValidator().validateSigList(block));
    }

}
