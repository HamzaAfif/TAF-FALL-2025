import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { TestResponseModel } from '../models/testResponseModel';
import { testModel2 } from '../models/testmodel2';
import { TestApiService } from './test-api.service';

describe('TestApiService', () => {
  let service: TestApiService;
  let httpMock: HttpTestingController;

  const buildTest = (id: number, method = 'GET'): testModel2 => ({
    id,
    method,
    apiUrl: `https://api.example.com/${id}`,
    headers: {},
    expectedHeaders: {},
    expectedOutput: 'ok',
    statusCode: 200,
  });

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TestApiService],
    });
    service = TestBed.inject(TestApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('executeTests should POST each test and return all responses', () => {
    const tests = [buildTest(1, 'GET'), buildTest(2, 'POST')];
    const responses: TestResponseModel[] = [
      {
        id: 1,
        stutsCode: 200,
        output: 'ok-1',
        fieldAnswer: null,
        answer: true,
        messages: [],
      },
      {
        id: 2,
        stutsCode: 201,
        output: 'ok-2',
        fieldAnswer: null,
        answer: false,
        messages: [],
      },
    ];

    let actual: TestResponseModel[] | undefined;
    service.executeTests(tests).subscribe((value) => {
      actual = value;
    });

    const requests = httpMock.match((req) =>
      req.url.endsWith('/team3/api/testapi/checkApi'),
    );
    expect(requests.length).toBe(2);

    const req1 = requests[0];
    expect(req1.request.method).toBe('POST');
    expect(req1.request.body.id).toBe(1);
    req1.flush(responses[0]);

    const req2 = requests[1];
    expect(req2.request.method).toBe('POST');
    expect(req2.request.body.id).toBe(2);
    req2.flush(responses[1]);

    expect(actual).toEqual(responses);
  });

  it('addTestOnList, getTest and deleteTest should manage list content', () => {
    const first = buildTest(0, 'GET');
    const second = buildTest(0, 'POST');

    service.addTestOnList(first);
    service.addTestOnList(second);

    expect(service.listTests.length).toBe(2);
    expect(service.listTests[0].id).toBe(1);
    expect(service.listTests[1].id).toBe(2);
    expect(service.getTest(2)?.method).toBe('POST');

    service.deleteTest(1);

    expect(service.listTests.length).toBe(1);
    expect(service.listTests[0].method).toBe('POST');
  });

  it('updateTestsStatusExecution should update responseStatus by index', () => {
    service.addTestOnList(buildTest(0, 'GET'));
    service.addTestOnList(buildTest(0, 'POST'));

    service.updateTestsStatusExecution([
      {
        id: 1,
        stutsCode: 200,
        output: 'A',
        fieldAnswer: null,
        answer: true,
        messages: [],
      },
      {
        id: 2,
        stutsCode: 200,
        output: 'B',
        fieldAnswer: null,
        answer: false,
        messages: [],
      },
    ]);

    expect(service.listTests[0].responseStatus).toBeTrue();
    expect(service.listTests[1].responseStatus).toBeFalse();
  });

  it('updateTestsStatusExecution should not update when lengths mismatch', () => {
    service.addTestOnList(buildTest(0, 'GET'));
    const errorSpy = spyOn(console, 'error');

    service.updateTestsStatusExecution([]);

    expect(errorSpy).toHaveBeenCalled();
    expect(service.listTests[0].responseStatus).toBeUndefined();
  });
});
