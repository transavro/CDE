// Code generated by protoc-gen-go. DO NOT EDIT.
// source: CDEService.proto

package CDEService

import (
	context "context"
	fmt "fmt"
	proto "github.com/golang/protobuf/proto"
	proto1 "github.com/transavro/ScheduleService/proto"
	_ "google.golang.org/genproto/googleapis/api/annotations"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
	math "math"
)

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

// This is a compile-time assertion to ensure that this generated file
// is compatible with the proto package it is being compiled against.
// A compilation error at this line likely means your copy of the
// proto package needs to be updated.
const _ = proto.ProtoPackageIsVersion3 // please upgrade the proto package

type SearchQuery struct {
	Query                string   `protobuf:"bytes,1,opt,name=query,proto3" json:"query,omitempty"`
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *SearchQuery) Reset()         { *m = SearchQuery{} }
func (m *SearchQuery) String() string { return proto.CompactTextString(m) }
func (*SearchQuery) ProtoMessage()    {}
func (*SearchQuery) Descriptor() ([]byte, []int) {
	return fileDescriptor_b71e49cbfefc313f, []int{0}
}

func (m *SearchQuery) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_SearchQuery.Unmarshal(m, b)
}
func (m *SearchQuery) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_SearchQuery.Marshal(b, m, deterministic)
}
func (m *SearchQuery) XXX_Merge(src proto.Message) {
	xxx_messageInfo_SearchQuery.Merge(m, src)
}
func (m *SearchQuery) XXX_Size() int {
	return xxx_messageInfo_SearchQuery.Size(m)
}
func (m *SearchQuery) XXX_DiscardUnknown() {
	xxx_messageInfo_SearchQuery.DiscardUnknown(m)
}

var xxx_messageInfo_SearchQuery proto.InternalMessageInfo

func (m *SearchQuery) GetQuery() string {
	if m != nil {
		return m.Query
	}
	return ""
}

type SearchResponse struct {
	ContentTile          []*proto1.Content `protobuf:"bytes,1,rep,name=contentTile,proto3" json:"contentTile,omitempty"`
	XXX_NoUnkeyedLiteral struct{}          `json:"-"`
	XXX_unrecognized     []byte            `json:"-"`
	XXX_sizecache        int32             `json:"-"`
}

func (m *SearchResponse) Reset()         { *m = SearchResponse{} }
func (m *SearchResponse) String() string { return proto.CompactTextString(m) }
func (*SearchResponse) ProtoMessage()    {}
func (*SearchResponse) Descriptor() ([]byte, []int) {
	return fileDescriptor_b71e49cbfefc313f, []int{1}
}

func (m *SearchResponse) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_SearchResponse.Unmarshal(m, b)
}
func (m *SearchResponse) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_SearchResponse.Marshal(b, m, deterministic)
}
func (m *SearchResponse) XXX_Merge(src proto.Message) {
	xxx_messageInfo_SearchResponse.Merge(m, src)
}
func (m *SearchResponse) XXX_Size() int {
	return xxx_messageInfo_SearchResponse.Size(m)
}
func (m *SearchResponse) XXX_DiscardUnknown() {
	xxx_messageInfo_SearchResponse.DiscardUnknown(m)
}

var xxx_messageInfo_SearchResponse proto.InternalMessageInfo

func (m *SearchResponse) GetContentTile() []*proto1.Content {
	if m != nil {
		return m.ContentTile
	}
	return nil
}

func init() {
	proto.RegisterType((*SearchQuery)(nil), "CDEService.SearchQuery")
	proto.RegisterType((*SearchResponse)(nil), "CDEService.SearchResponse")
}

func init() { proto.RegisterFile("CDEService.proto", fileDescriptor_b71e49cbfefc313f) }

var fileDescriptor_b71e49cbfefc313f = []byte{
	// 271 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0x74, 0x90, 0x41, 0x4a, 0x03, 0x31,
	0x14, 0x86, 0x89, 0x62, 0xc5, 0xb4, 0x8a, 0x04, 0xc1, 0x71, 0x70, 0x51, 0xe2, 0xa6, 0x74, 0x31,
	0x91, 0xba, 0xab, 0x2b, 0xa9, 0x2e, 0x5d, 0x74, 0xc6, 0xb5, 0x90, 0xc6, 0xc7, 0x4c, 0x60, 0x9a,
	0x37, 0x26, 0x99, 0x82, 0x5b, 0xaf, 0xe0, 0x81, 0x3c, 0x84, 0x57, 0xf0, 0x20, 0xe2, 0xa4, 0xc5,
	0x48, 0xe9, 0x2e, 0x79, 0xf9, 0xf8, 0x5e, 0xfe, 0x9f, 0x9e, 0xce, 0xee, 0x1f, 0x0a, 0xb0, 0x2b,
	0xad, 0x20, 0x6b, 0x2c, 0x7a, 0x64, 0xf4, 0x6f, 0x92, 0x5e, 0x96, 0x88, 0x65, 0x0d, 0x42, 0x36,
	0x5a, 0x48, 0x63, 0xd0, 0x4b, 0xaf, 0xd1, 0xb8, 0x40, 0xa6, 0x77, 0xa5, 0xf6, 0x55, 0xbb, 0xc8,
	0x14, 0x2e, 0x85, 0xb7, 0xd2, 0x38, 0xb9, 0xb2, 0x28, 0x0a, 0x55, 0xc1, 0x4b, 0x5b, 0xc3, 0xda,
	0x21, 0x3a, 0x76, 0x33, 0x95, 0xf6, 0xdf, 0x32, 0x7e, 0x45, 0xfb, 0x05, 0x48, 0xab, 0xaa, 0x79,
	0x0b, 0xf6, 0x8d, 0x9d, 0xd1, 0x83, 0xd7, 0xdf, 0x43, 0x42, 0x86, 0x64, 0x74, 0x94, 0x87, 0x0b,
	0x7f, 0xa4, 0x27, 0x01, 0xca, 0xc1, 0x35, 0x68, 0x1c, 0xb0, 0x5b, 0xda, 0x57, 0x68, 0x3c, 0x18,
	0xff, 0xa4, 0x6b, 0x48, 0xc8, 0x70, 0x7f, 0xd4, 0x9f, 0x5c, 0x64, 0x5b, 0x4b, 0x66, 0x01, 0xca,
	0x63, 0x7a, 0xf2, 0x49, 0x68, 0x94, 0x91, 0x3d, 0xd3, 0x41, 0xb0, 0x17, 0xde, 0x82, 0x5c, 0xb2,
	0xf3, 0x2c, 0xaa, 0x24, 0xfa, 0x5c, 0xba, 0xdb, 0xcf, 0x93, 0xf7, 0xaf, 0xef, 0x8f, 0x3d, 0xc6,
	0x8f, 0x85, 0x8b, 0x54, 0x53, 0x32, 0xbe, 0x26, 0x6c, 0x4e, 0x7b, 0xc1, 0xb2, 0xdb, 0x9c, 0x6e,
	0x3f, 0x6c, 0xa2, 0x72, 0xd6, 0xa9, 0x07, 0xfc, 0x70, 0xad, 0x9e, 0x92, 0xf1, 0xa2, 0xd7, 0x95,
	0x77, 0xf3, 0x13, 0x00, 0x00, 0xff, 0xff, 0x1d, 0xf1, 0x96, 0x4a, 0xbd, 0x01, 0x00, 0x00,
}

// Reference imports to suppress errors if they are not otherwise used.
var _ context.Context
var _ grpc.ClientConn

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
const _ = grpc.SupportPackageIsVersion4

// CDEServiceClient is the client API for CDEService service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://godoc.org/google.golang.org/grpc#ClientConn.NewStream.
type CDEServiceClient interface {
	SearchStream(ctx context.Context, in *SearchQuery, opts ...grpc.CallOption) (CDEService_SearchStreamClient, error)
	Search(ctx context.Context, in *SearchQuery, opts ...grpc.CallOption) (*SearchResponse, error)
}

type cDEServiceClient struct {
	cc *grpc.ClientConn
}

func NewCDEServiceClient(cc *grpc.ClientConn) CDEServiceClient {
	return &cDEServiceClient{cc}
}

func (c *cDEServiceClient) SearchStream(ctx context.Context, in *SearchQuery, opts ...grpc.CallOption) (CDEService_SearchStreamClient, error) {
	stream, err := c.cc.NewStream(ctx, &_CDEService_serviceDesc.Streams[0], "/CDEService.CDEService/SearchStream", opts...)
	if err != nil {
		return nil, err
	}
	x := &cDEServiceSearchStreamClient{stream}
	if err := x.ClientStream.SendMsg(in); err != nil {
		return nil, err
	}
	if err := x.ClientStream.CloseSend(); err != nil {
		return nil, err
	}
	return x, nil
}

type CDEService_SearchStreamClient interface {
	Recv() (*proto1.Content, error)
	grpc.ClientStream
}

type cDEServiceSearchStreamClient struct {
	grpc.ClientStream
}

func (x *cDEServiceSearchStreamClient) Recv() (*proto1.Content, error) {
	m := new(proto1.Content)
	if err := x.ClientStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

func (c *cDEServiceClient) Search(ctx context.Context, in *SearchQuery, opts ...grpc.CallOption) (*SearchResponse, error) {
	out := new(SearchResponse)
	err := c.cc.Invoke(ctx, "/CDEService.CDEService/Search", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// CDEServiceServer is the server API for CDEService service.
type CDEServiceServer interface {
	SearchStream(*SearchQuery, CDEService_SearchStreamServer) error
	Search(context.Context, *SearchQuery) (*SearchResponse, error)
}

// UnimplementedCDEServiceServer can be embedded to have forward compatible implementations.
type UnimplementedCDEServiceServer struct {
}

func (*UnimplementedCDEServiceServer) SearchStream(req *SearchQuery, srv CDEService_SearchStreamServer) error {
	return status.Errorf(codes.Unimplemented, "method SearchStream not implemented")
}
func (*UnimplementedCDEServiceServer) Search(ctx context.Context, req *SearchQuery) (*SearchResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Search not implemented")
}

func RegisterCDEServiceServer(s *grpc.Server, srv CDEServiceServer) {
	s.RegisterService(&_CDEService_serviceDesc, srv)
}

func _CDEService_SearchStream_Handler(srv interface{}, stream grpc.ServerStream) error {
	m := new(SearchQuery)
	if err := stream.RecvMsg(m); err != nil {
		return err
	}
	return srv.(CDEServiceServer).SearchStream(m, &cDEServiceSearchStreamServer{stream})
}

type CDEService_SearchStreamServer interface {
	Send(*proto1.Content) error
	grpc.ServerStream
}

type cDEServiceSearchStreamServer struct {
	grpc.ServerStream
}

func (x *cDEServiceSearchStreamServer) Send(m *proto1.Content) error {
	return x.ServerStream.SendMsg(m)
}

func _CDEService_Search_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(SearchQuery)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(CDEServiceServer).Search(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/CDEService.CDEService/Search",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(CDEServiceServer).Search(ctx, req.(*SearchQuery))
	}
	return interceptor(ctx, in, info, handler)
}

var _CDEService_serviceDesc = grpc.ServiceDesc{
	ServiceName: "CDEService.CDEService",
	HandlerType: (*CDEServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "Search",
			Handler:    _CDEService_Search_Handler,
		},
	},
	Streams: []grpc.StreamDesc{
		{
			StreamName:    "SearchStream",
			Handler:       _CDEService_SearchStream_Handler,
			ServerStreams: true,
		},
	},
	Metadata: "CDEService.proto",
}
