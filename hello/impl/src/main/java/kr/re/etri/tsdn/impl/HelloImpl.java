package kr.re.etri.tsdn.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RoutedRpcRegistration;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.NodeContext;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.NodeType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.NodesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.RoutedrpcTestInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.RoutedrpcTestInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.RpcInputtestInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.nodes.NodeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.nodes.NodeKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.Futures;

public class HelloImpl implements HelloService {

	private static final Logger LOG = LoggerFactory.getLogger(HelloImpl.class);
	ProviderContext session = null;
    private DataBroker db;
    
	public static final InstanceIdentifier<Nodes> NODES_IID = 
			InstanceIdentifier.builder(Nodes.class).build();
	
	public DataBroker getDb() {
		return db;
	}

	public void setDb(DataBroker db) {
		this.db = db;
	}

	public ProviderContext getSession() {
		return session;
	}

	public void setSession(ProviderContext session) {
		this.session = session;
	}

	@Override
	public Future<RpcResult<Void>> nodeUpdate() {
		// TODO Auto-generated method stub
		
		
		
		LOG.info("nodeUpdated by hello-impl");
		final ReadWriteTransaction tx = db.newReadWriteTransaction();
		
//		LOG.info("[jshin]HELLO_IID: " + HELLO_IID);
//		QName nodes = QName.create("urn:opendaylight:params:xml:ns:yang:hello","2015-01-05","nodes");
//		QName node = QName.create(nodes,"nodes");
//		QName idName = QName.create(nodes,"id");

		
		List<Node> listOfNodes = new LinkedList<Node>();
		Node node1 = new NodeBuilder().setId(new NodeId("labry")).setKey(new NodeKey(new NodeId("labry"))).setType(NodeType.Ptn).build();
		Node node2 = new NodeBuilder().setId(new NodeId("jshin")).setKey(new NodeKey(new NodeId("jshin"))).setType(NodeType.Otn).build();
		listOfNodes.add(node1);
		listOfNodes.add(node2);
		
		tx.put(LogicalDatastoreType.OPERATIONAL, NODES_IID, 
				new NodesBuilder().setNode(listOfNodes).build());
		
		try {
			tx.submit().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
 		NodeKey nodeKey = new NodeKey(new NodeId("labry"));
		InstanceIdentifier<Node> path = InstanceIdentifier.create(Nodes.class).child(Node.class, nodeKey);
		RoutedRpcRegistration<HelloService> reg1 =
			      session.addRoutedRpcImplementation(
			        HelloService.class, this);
		
		reg1.registerPath(NodeContext.class ,path);


		
		return Futures.immediateFuture( RpcResultBuilder.<Void> success().build() );
	}

	@Override
	public Future<RpcResult<Void>> routedrpcTest(RoutedrpcTestInput input) {
		LOG.info("routedrpcTest by hello-impl");
		return Futures.immediateFuture( RpcResultBuilder.<Void> success().build() );
	}

	@Override
	public Future<RpcResult<Void>> rpcInputtest(RpcInputtestInput input) {
		
		String str = input.getStrin();
		RoutedrpcTestInput in = new RoutedrpcTestInputBuilder().setNodeRef(new NodeRef(NODES_IID)).build();
		LOG.info("rpcInputtest by hello-impl: " + str);
		LOG.info("RoutedrpcTestInput:" + in.getNodeRef().toString());
		return Futures.immediateFuture( RpcResultBuilder.<Void> success().build() );
	}
}

